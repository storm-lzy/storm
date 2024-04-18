package tree;

/**
 *
 */
public class MirrorTree {

    public static boolean mirrorTree(TreeNode p,TreeNode q){
        if(p == null ^ q == null){
            return false;
        }
        if(p == null){
            return true;
        }
        return p.v == q.v && mirrorTree(p.left,q.right) && mirrorTree(p.right,q.left);
    }
}
