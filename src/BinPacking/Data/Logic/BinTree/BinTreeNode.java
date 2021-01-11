package BinPacking.Data.Logic.BinTree;

import BinPacking.Data.Logic.Bin.Bin;
import BinPacking.Data.Logic.Box.Box;
import BinPacking.DependencyInjectors.BinInjector;
import BinPacking.Logic.PackingStrategy.PackingStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Xsignati on 27.05.2017.
 */
public class BinTreeNode implements BinTree{
    private static int rootNodesCounter;
    private BinTree parent;
    private List<BinTree> children;
    private Bin bin;
    private int id;

    private BinTreeNode(Bin bin) {
        id = rootNodesCounter;
        this.bin = bin;
        bin.setId(id);
        this.children = new LinkedList<>();
    }

    @Override
    public void addChildWith(Bin child){
        BinTree childNode = new BinTreeNode(child);
        childNode.setParent(this);
        childNode.setId(id);
        children.add(childNode);
    }

    @Override
    public void setParent(BinTree binTree){
        parent = binTree;
    }

    @Override
    public List<BinTree> getChildren(){
        return children;
    }

    @Override
    public Bin getData(){
        return bin;
    }

    @Override
    public void setId(int id){
        this.id = id;
    }

    @Override
    public int getId(){
        return id;
    }

    public static BinTree rootNode(Bin bin){
        rootNodesCounter++;
        return new BinTreeNode(bin);
    }

    /**
     * Every box insertion creates 12 new bins (3 subspaces in XYZ direction in 4 alternate versions). Each triplet has its BinType (A,B,C or D) exclusive to rest.
     * If one BinType is chosen the rest must be removed. The BinTypes represent new spaces created after box insertion.
     * @param box Box object.
     */
    // 在box被加入到bin以后，添加12个子空间（可能重合），每一面都会有4个子空间如下：
    // ********************************** ^
    // *                                * |
    // *                                * |
    // *                                * |
    // *                                * |
    // *                                * bin height
    // *                         ******** | ^
    // *                         *      * | |
    // *                         *      * | box height
    // *                         *      * | |
    // ********************************** | |
    //                          box width
    // <-----------bin width------------>

    // 这里举例一个面，4个可能性为 box width * box height; box width * bin height;
    // bin width * box height; bin width * bin height
    // 会在当前bin的children里面，存入subspace，包括它的长宽高以及在整个bin里的坐标起点
    // 这里要注意，每个方向的同一个type组合起来+原有的box即构成了当前的bin
    // 所以在删除的时候，一旦一个方向的一个type被选中要放入这个box了，那么其他type就不能再被选了，只会留下同一个type的互补的部分

    public void tryToAddSubspacesFor(Box box) {
        if (isFirstArgGreater(bin.getLength(), box.getLength())) {
            addChildWith(BinInjector.get(bin.getX() + box.getLength(), bin.getY(), bin.getZ(), bin.getLength() - box.getLength(), bin.getWidth(), bin.getHeight(), Bin.Type.A));
            addChildWith(BinInjector.get(bin.getX() + box.getLength(), bin.getY(), bin.getZ(), bin.getLength() - box.getLength(), box.getWidth(), bin.getHeight(), Bin.Type.B));
            addChildWith(BinInjector.get(bin.getX() + box.getLength(), bin.getY(), bin.getZ(), bin.getLength() - box.getLength(), bin.getWidth(), box.getHeight(), Bin.Type.C));
            addChildWith(BinInjector.get(bin.getX() + box.getLength(), bin.getY(), bin.getZ(), bin.getLength() - box.getLength(), box.getWidth(), box.getHeight(), Bin.Type.D));
        }
        if (isFirstArgGreater(bin.getWidth(), box.getWidth())) {
            addChildWith(BinInjector.get(bin.getX(), bin.getY() + box.getWidth(), bin.getZ(), box.getLength(), bin.getWidth() - box.getWidth(), bin.getHeight(), Bin.Type.A));
            addChildWith(BinInjector.get(bin.getX(), bin.getY() + box.getWidth(), bin.getZ(), bin.getLength(), bin.getWidth() - box.getWidth(), bin.getHeight(), Bin.Type.B));
            addChildWith(BinInjector.get(bin.getX(), bin.getY() + box.getWidth(), bin.getZ(), box.getLength(), bin.getWidth() - box.getWidth(), box.getHeight(), Bin.Type.C));
            addChildWith(BinInjector.get(bin.getX(), bin.getY() + box.getWidth(), bin.getZ(), bin.getLength(), bin.getWidth() - box.getWidth(), box.getHeight(), Bin.Type.D));
        }
        if (isFirstArgGreater(bin.getHeight(), box.getHeight())) {
            addChildWith(BinInjector.get(bin.getX(), bin.getY(), bin.getZ() + box.getHeight(), box.getLength(), box.getWidth(), bin.getHeight() - box.getHeight(), Bin.Type.A));
            addChildWith(BinInjector.get(bin.getX(), bin.getY(), bin.getZ() + box.getHeight(), box.getLength(), box.getWidth(), bin.getHeight() - box.getHeight(), Bin.Type.B));
            addChildWith(BinInjector.get(bin.getX(), bin.getY(), bin.getZ() + box.getHeight(), bin.getLength(), bin.getWidth(), bin.getHeight() - box.getHeight(), Bin.Type.C));
            addChildWith(BinInjector.get(bin.getX(), bin.getY(), bin.getZ() + box.getHeight(), bin.getLength(), bin.getWidth(), bin.getHeight() - box.getHeight(), Bin.Type.D));
        }
    }

    private boolean isFirstArgGreater(double first, double second){
        return first > second;
    }

    public void removeNotSelectedSubspaces() {
        if (parentExists())
            parent.getChildren().removeIf(node -> node.getData().getType() != bin.getType());
    }

    private boolean parentExists(){
        return parent != null;
    }

    public void reserveBinFor(Box box){
        giveCoordinatesAndIdTo(box);
        changeStateToFull();
    }

    private void giveCoordinatesAndIdTo(Box box){
        box.setCoordinates(bin.getX(), bin.getY(), bin.getZ());
        box.setId(id);
    }

    private void changeStateToFull(){
        bin.setState(Bin.State.FULL);
    }

    public BinTree search(PackingStrategy packingStrategy, Box box) {
        return packingStrategy.search(this, box);
    }
}