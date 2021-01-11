package BinPacking.Logic.PackingStrategy;

import BinPacking.Data.Logic.BinTree.BinTree;
import javafx.collections.ObservableList;
import BinPacking.Data.Logic.Bin.Bin;
import BinPacking.Data.Logic.Box.Box;

/**
 * Created by Xsignati on 24.01.2017.
 * Find and return the Bin with minimum volume.
 */
public class BestFit implements PackingStrategy {
    @Override
    // important: Best Fit 的核心搜索逻辑
    // 深度优先的树搜索，会遍历所有的子节点，找到最合适的点
    public BinTree search(BinTree binTreeNode, Box box) {
        BinTree minBinNode;

        if(isBinEmptyAndFitToBox(binTreeNode, box))
            minBinNode = binTreeNode;
        else
            minBinNode = null;

        for (BinTree child : binTreeNode.getChildren())
            minBinNode = min(minBinNode, search(child, box));

        return minBinNode;
    }

    private boolean isBinEmptyAndFitToBox(BinTree binTreeNode, Box box){
        Bin bin = binTreeNode.getData();
        return bin.getState() == Bin.State.EMPTY && boxFitsToBin(bin, box);
    }

    // min是会递归调用的，会找到最合适的
    private BinTree min(BinTree firstNode, BinTree secondNode){
        BinTree minimumVolumeNode;
        if(isNull(firstNode))
            minimumVolumeNode = secondNode;
        else
            minimumVolumeNode = continueSearch(firstNode, secondNode);

        return minimumVolumeNode;
    }

    private boolean isNull(BinTree node){
        return node == null;
    }

    private BinTree continueSearch(BinTree firstNode, BinTree secondNode){
        if(isNull(secondNode))
            return firstNode;
        else
            return getSmallerVolumeNode(firstNode, secondNode);
    }

    private BinTree getSmallerVolumeNode(BinTree firstNode, BinTree secondNode){
        Bin first = firstNode.getData();
        Bin second = secondNode.getData();
        if(isVolumeGreater(first, second))
            // 因为是Best fit, 如果first的可用空间大于second的可用空间，那么选择更加fit的secondNode
            return secondNode;
        else
            return firstNode;
    }

    private boolean isVolumeGreater(Bin first, Bin second){
        return second.getVolume() < first.getVolume();
    }

    @Override
    public void prepareInput(ObservableList<Box> boxList){
        boxList.sort((b1,b2) -> b2.getVolume().compareTo(b1.getVolume()));
    }
}

