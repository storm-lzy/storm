package com.storm.mq.utils;

import cn.hutool.cache.impl.AbstractCache;
import cn.hutool.core.util.StrUtil;
import com.storm.mq.cache.Cache;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public final class HostInfo {
    private static final byte[] DEFAULT_SUBNET_MASK = new byte[]{-1, -1, -1, 0};
    private static final int IPV4_LEN = 4;
    private static final long ADDRESS_A_MIN = 65536L;
    private static final long ADDRESS_A_MAX = 2130706431L;
    private static final long ADDRESS_B_MIN = -2147483648L;
    private static final long ADDRESS_B_MAX = -1073741825L;
    private static final long ADDRESS_C_MIN = -1073741824L;
    private static final long ADDRESS_C_MAX = -536870913L;
    private static final long ADDRESS_D_MIN = -536870912L;
    private static final long ADDRESS_D_MAX = -268435457L;

    private static final long REFRESH_TIME;
    private static final int TIMEOUT = 100;
    private static volatile Cache<List<HostInfo>> ALL_HOST_INFO_CACHE;
    private final String hostName;
    private final String hostAddress;
    private final byte[] address;
    private final byte[] subnetMask;
    private final IpType type;
    private final int subnetId;
    private InetAddress inetAddress;
    private final NetworkInterface networkInterface;

    private HostInfo(InetAddress inetAddress, byte[] subnetMask, NetworkInterface networkInterface) {
        this.inetAddress = inetAddress;
        this.hostName = inetAddress.getHostName();
        this.hostAddress = inetAddress.getHostAddress();
        this.address = inetAddress.getAddress();
        this.subnetMask = subnetMask;
        this.type = getType(this.address);
        this.subnetId = NumberUtil.mergeToInt(this.address) & ~NumberUtil.mergeToInt(subnetMask);
        this.networkInterface = networkInterface;
    }

    public static HostInfo getInstance() {
        return (HostInfo) ((List<?>) ALL_HOST_INFO_CACHE.getTarget()).get(0);
    }

    public static List<?> getAll() {
        return ALL_HOST_INFO_CACHE.getTarget();
    }

    public static IpType getType(byte[] address) {
        Assert.isTrue(address != null && address.length == 4, "当前数据不是IPv4地址：" + (address == null ? "null" : Arrays.toString(address)));
        long target = (long) NumberUtil.mergeToInt(address);
        if (target >= 65536L && target <= 2130706431L) {
            return HostInfo.IpType.TYPE_A;
        } else if (target <= -1073741825L) {
            return HostInfo.IpType.TYPE_B;
        } else if (target <= -536870913L) {
            return HostInfo.IpType.TYPE_C;
        } else {
            return target <= -268435457L ? HostInfo.IpType.TYPE_D : HostInfo.IpType.TYPE_E;
        }
    }

    private static List<HostInfo> obtainAll() {
        List<HostInfo> allHostInfo = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            List<Pair<List<InterfaceAddress>, NetworkInterface>> allInterface = new ArrayList<>();

            label81:
            while (true) {
                NetworkInterface networkInterface;
                String displayName;
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!networkInterfaceEnumeration.hasMoreElements()) {
                                        if (!allInterface.isEmpty()) {
                                            allInterface.sort(HostInfo::compare);

                                            for (Pair<List<InterfaceAddress>, NetworkInterface> listNetworkInterfacePair : allInterface) {
                                                for (InterfaceAddress o : listNetworkInterfacePair.getKey()) {
                                                    InetAddress inetAddress = o.getAddress();
                                                    allHostInfo.add(new HostInfo(inetAddress, calcSubnetMask(o), listNetworkInterfacePair.getValue()));
                                                }
                                            }
                                        } else {
                                            allHostInfo.add(getLocal());
                                        }
                                        break label81;
                                    }

                                    networkInterface = (NetworkInterface) networkInterfaceEnumeration.nextElement();
                                } while (networkInterface.isPointToPoint());
                            } while (networkInterface.isLoopback());

                            displayName = StrUtil.blankToDefault(networkInterface.getDisplayName(), "");
                        } while (displayName.contains("Hyper-V"));
                    } while (displayName.contains("VirtualBox"));
                } while (displayName.contains("VMware"));

                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                List<InterfaceAddress> usedInterfaceAddress = new ArrayList<>();

                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    InetAddress inetAddress = interfaceAddress.getAddress();
                    if (!(inetAddress instanceof Inet6Address) && !inetAddress.isAnyLocalAddress()) {
                        usedInterfaceAddress.add(interfaceAddress);
                    }
                }

                if (!usedInterfaceAddress.isEmpty()) {
                    allInterface.add(new Pair<>(usedInterfaceAddress, networkInterface));
                }
            }
        } catch (Throwable var10) {
            allHostInfo.add(getLocal());
        }

        allHostInfo.sort(HostInfo::compare);
        return Collections.unmodifiableList(allHostInfo);
    }

    private static byte[] calcSubnetMask(InterfaceAddress interfaceAddress) {
        short networkPrefixLength = interfaceAddress.getNetworkPrefixLength();
        int subnetMask = 0;
        int i = 0;

        for (int j = 31; i < networkPrefixLength; --j) {
            subnetMask |= 1 << j;
            ++i;
        }

        return NumberUtil.splitToByte(subnetMask);
    }

    private static int compare(HostInfo h1, HostInfo h2) {
        return h1.type.order - h2.type.order;
    }

    private static int compare(Pair<List<InterfaceAddress>, NetworkInterface> o1, Pair<List<InterfaceAddress>, NetworkInterface> o2) {
        return ((NetworkInterface) o1.getValue()).getIndex() - ((NetworkInterface) o2.getValue()).getIndex();
    }

    private static HostInfo getLocal() {
        InetAddress localhost;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (Exception var2) {
            localhost = InetAddress.getLoopbackAddress();
        }

        return new HostInfo(localhost, DEFAULT_SUBNET_MASK, null);
    }

    public String getHostName() {
        return this.hostName;
    }

    public String getHostAddress() {
        return this.hostAddress;
    }

    public byte[] getAddress() {
        return this.address;
    }

    public byte[] getSubnetMask() {
        return this.subnetMask;
    }

    public IpType getType() {
        return this.type;
    }

    public int getSubnetId() {
        return this.subnetId;
    }

    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    public NetworkInterface getNetworkInterface() {
        return this.networkInterface;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof HostInfo)) {
            return false;
        } else {
            HostInfo other = (HostInfo) o;
            if (this.getSubnetId() != other.getSubnetId()) {
                return false;
            } else {
                label79:
                {
                    Object this$hostName = this.getHostName();
                    Object other$hostName = other.getHostName();
                    if (this$hostName == null) {
                        if (other$hostName == null) {
                            break label79;
                        }
                    } else if (this$hostName.equals(other$hostName)) {
                        break label79;
                    }

                    return false;
                }

                Object this$hostAddress = this.getHostAddress();
                Object other$hostAddress = other.getHostAddress();
                if (this$hostAddress == null) {
                    if (other$hostAddress != null) {
                        return false;
                    }
                } else if (!this$hostAddress.equals(other$hostAddress)) {
                    return false;
                }

                if (!Arrays.equals(this.getAddress(), other.getAddress())) {
                    return false;
                } else if (!Arrays.equals(this.getSubnetMask(), other.getSubnetMask())) {
                    return false;
                } else {
                    label62:
                    {
                        Object this$type = this.getType();
                        Object other$type = other.getType();
                        if (this$type == null) {
                            if (other$type == null) {
                                break label62;
                            }
                        } else if (this$type.equals(other$type)) {
                            break label62;
                        }

                        return false;
                    }

                    label55:
                    {
                        Object this$inetAddress = this.getInetAddress();
                        Object other$inetAddress = other.getInetAddress();
                        if (this$inetAddress == null) {
                            if (other$inetAddress == null) {
                                break label55;
                            }
                        } else if (this$inetAddress.equals(other$inetAddress)) {
                            break label55;
                        }

                        return false;
                    }

                    Object this$networkInterface = this.getNetworkInterface();
                    Object other$networkInterface = other.getNetworkInterface();
                    if (this$networkInterface == null) {
                        if (other$networkInterface != null) {
                            return false;
                        }
                    } else if (!this$networkInterface.equals(other$networkInterface)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getSubnetId();
        Object $hostName = this.getHostName();
        result = result * 59 + ($hostName == null ? 43 : $hostName.hashCode());
        Object $hostAddress = this.getHostAddress();
        result = result * 59 + ($hostAddress == null ? 43 : $hostAddress.hashCode());
        result = result * 59 + Arrays.hashCode(this.getAddress());
        result = result * 59 + Arrays.hashCode(this.getSubnetMask());
        Object $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Object $inetAddress = this.getInetAddress();
        result = result * 59 + ($inetAddress == null ? 43 : $inetAddress.hashCode());
        Object $networkInterface = this.getNetworkInterface();
        result = result * 59 + ($networkInterface == null ? 43 : $networkInterface.hashCode());
        return result;
    }

    public String toString() {
        return "HostInfo(hostName=" + this.getHostName() + ", hostAddress=" + this.getHostAddress() + ", address=" + Arrays.toString(this.getAddress()) + ", subnetMask=" + Arrays.toString(this.getSubnetMask()) + ", type=" + this.getType() + ", subnetId=" + this.getSubnetId() + ", inetAddress=" + this.getInetAddress() + ", networkInterface=" + this.getNetworkInterface() + ")";
    }

    static {
        REFRESH_TIME = TimeUnit.MINUTES.toMillis(5L);
        ALL_HOST_INFO_CACHE = HostInfo::obtainAll;
    }

    public static enum IpType {
        TYPE_A(0),
        TYPE_B(1),
        TYPE_C(2),
        TYPE_D(3),
        TYPE_E(4);

        private final int order;

        private IpType(int order) {
            this.order = order;
        }
    }

    public static class NumberUtil {
        public NumberUtil() {
        }

        public static int mergeToInt(byte[] data) {
            return mergeToInt(data, 0);
        }

        public static int mergeToInt(byte[] data, int offset) {
            return Byte.toUnsignedInt(data[offset]) << 24 | Byte.toUnsignedInt(data[1 + offset]) << 16 | Byte.toUnsignedInt(data[2 + offset]) << 8 | Byte.toUnsignedInt(data[3 + offset]);
        }

        public static byte[] splitToByte(int data) {
            byte[] result = new byte[]{(byte) (data >>> 24 & 255), (byte) (data >>> 16 & 255), (byte) (data >>> 8 & 255), (byte) (data & 255)};
            return result;
        }
    }


}