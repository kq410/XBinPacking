package BinPacking.Logic.Packer;

import BinPacking.Data.Logic.BinTree.BinTree;
import BinPacking.Data.Logic.BinTree.BinTreeNode;
import BinPacking.Data.Logic.Box.Box;
import BinPacking.Data.Logic.InputData.InputData;
import BinPacking.Data.Logic.Rotation.BoxRotator;
import BinPacking.DependencyInjectors.BinInjector;

/**
 * Created by Xsignati on 24.01.2017.
 * Main logic class responsible for assigning boxes to binTrees.
 */
//这里packer用接口实现，方便后续添加新的packing逻辑
public class BinPacker implements Packer {
    // 算法主逻辑接口pack
    public void pack(InputData inputData) {
        //创建root bin并且对box进行预处理（排序）
        prepareInputAndAddBin(inputData);
        // 遍历boxList，进行packing
        for(Box box: inputData.getBoxList()) {
            int binListSize = inputData.getBinList().size();
            // pack的具体逻辑：fitBoxOrCreateBinTree
            fitBoxOrCreateBinTree(inputData, box, binListSize);
        }
    }

    private void prepareInputAndAddBin(InputData inputData){
        inputData.getPackingStrategy().prepareInput(inputData.getBoxList());
        inputData.getBinList().add(BinTreeNode.rootNode(BinInjector.get(inputData.getBinLength(), inputData.getBinWidth(), inputData.getBinHeight())));
    }
    // box被装入bin中的具体实现：
    private void fitBoxOrCreateBinTree(InputData inputData, Box box, int binListSize){
        // binList里包括每个开辟的bin
        for(int currentNode = 0 ; currentNode <= binListSize; currentNode++){
            // 在当前bin中或者bin的children以及其children中是否能找到合适的位置放入这个box
            // important: 核心逻辑入口
            if(BoxFitsToBin(inputData, box, currentNode))
                // 如果当前的Node所属的bin能够装载box,则放入然后return
                return;
            else if (allNodesAreToSmall(currentNode, binListSize))
                // 如果所有的bin都无法承载该box,则开辟一个新的bin
                createNewBinTree(inputData);

        }
    }

    private boolean allNodesAreToSmall(int currentNode, int binListSize){
        return currentNode == binListSize - 1;
    }

    private boolean BoxFitsToBin(InputData inputData, Box box, int currentNode){
        for (int currentRotation = 0; currentRotation < BoxRotator.ROTATIONS_NUM; currentRotation++) {
            // 根据不同启发（PackingStrategy) bestArea/FirstFit，找到最合适的摆放位置
            BinTree foundNode = inputData.getBinList().get(currentNode).search(inputData.getPackingStrategy(), box);
            if (nodeExists(foundNode)) {
                //
                foundNode.reserveBinFor(box);
                foundNode.tryToAddSubspacesFor(box);
                foundNode.removeNotSelectedSubspaces();
                return true;
            }
            else
                box.rotate();
        }
        return false;
    }

    private void createNewBinTree(InputData inputData){
        inputData.getBinList().add(BinTreeNode.rootNode(BinInjector.get(inputData.getBinLength(), inputData.getBinWidth(), inputData.getBinHeight())));
    }

    private boolean nodeExists(BinTree foundNode){
        return foundNode != null;
    }
}

