package BinPacking.Logic.PackingStrategy;

import BinPacking.Data.Logic.BinTree.BinTree;
import javafx.collections.ObservableList;
import BinPacking.Data.Logic.Bin.Bin;
import BinPacking.Data.Logic.Box.Box;

/**
 * Created by Xsignati on 24.01.2017.
 */
// TODO: AreaBestFit 中continueSearch有点问题
public class AreaBestFit implements PackingStrategy {
    @Override
    public BinTree search(BinTree binTreeNode, Box box)  {
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
            return getSmallerZandVolume(firstNode, secondNode);
    }

    private BinTree getSmallerZandVolume(BinTree firstNode, BinTree secondNode){
        Bin first = firstNode.getData();
        Bin second = secondNode.getData();
        if(areZEqual(first, second))
            return getSmallerVolumeNode(firstNode, secondNode);
        else
            return firstNode;
    }

    private boolean areZEqual(Bin first, Bin second){return first.getZ() == second.getZ();}

    private BinTree getSmallerVolumeNode(BinTree firstNode, BinTree secondNode){
        Bin first = firstNode.getData();
        Bin second = secondNode.getData();
        if(isFirstGreater(first, second))
            return secondNode;
        else if(isFirstZgreater(first, second))
            return secondNode;
        else
            return firstNode;
    }

    private boolean isFirstGreater(Bin first, Bin second){
        return second.getVolume() < first.getVolume();
    }

    private boolean isFirstZgreater(Bin first, Bin second){
        return second.getZ() < first.getZ();
    }

    @Override
    public void prepareInput(ObservableList<Box> boxList){
        boxList.sort((b1,b2) -> b2.getVolume().compareTo(b1.getVolume()));
    }

}
