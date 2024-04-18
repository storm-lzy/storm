package tree;

/**
 *
 */
public class BinaryTree {

    public static void pre(TreeNode node){
        if(node == null){
            return;
        }
        System.out.println(node.v);
        pre(node);
        pre(node);
    }
    public static void mde(TreeNode node){
        if(node == null){
            return;
        }
        mde(node);
        System.out.println(node.v);
        mde(node);
    }
    public static void after(TreeNode node){
        if(node == null){
            return;
        }
        after(node);
        after(node);
        System.out.println(node.v);
    }
}
