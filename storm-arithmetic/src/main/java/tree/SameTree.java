package tree;

/**
 *
 */
public class SameTree {

    public static boolean sameTree(TreeNode p,TreeNode q){
        if(p == null ^ q == null){
            return false;
        }
        if(p == null){
            return true;
        }
        return q.v == p.v && sameTree(p.left,q.left) && sameTree(p.right,q.right);
    }

}
