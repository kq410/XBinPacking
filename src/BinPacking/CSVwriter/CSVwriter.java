package BinPacking.CSVwriter;

import BinPacking.Data.Logic.BinTree.BinTree;
import BinPacking.Data.Logic.Box.Box;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;

public class CSVwriter {
    private final ObservableList<Box> boxList;
    private final ObservableList<BinTree> binList;
    private final String path;



    public CSVwriter(ObservableList<Box> boxList, ObservableList<BinTree> binList, String path) {
        this.boxList = boxList;
        this.binList = binList;
        this.path = path;
    }

    public void WriteAllData() throws IOException{
        WriteBinData();
        WriteBoxData();
    }

    public void WriteBoxData() throws IOException {
        FileWriter csvWriter = new FileWriter(getPath() + "/boxdata.csv");
        csvWriter.append("binID");
        csvWriter.append(",");
        csvWriter.append("x");
        csvWriter.append(",");
        csvWriter.append("y");
        csvWriter.append(",");
        csvWriter.append("z");
        csvWriter.append(",");
        csvWriter.append("length");
        csvWriter.append(",");
        csvWriter.append("width");
        csvWriter.append(",");
        csvWriter.append("height");
        csvWriter.append("\n");
        PoorBoxDataInto(csvWriter);
        csvWriter.flush();
        csvWriter.close();
    }

    public void WriteBinData() throws IOException{
        FileWriter csvWriter = new FileWriter(getPath() + "/bindata.csv");
        csvWriter.append("binID");
        csvWriter.append(",");
        csvWriter.append("length");
        csvWriter.append(",");
        csvWriter.append("width");
        csvWriter.append(",");
        csvWriter.append("height");
        csvWriter.append("\n");
        PoorBinDatInto(csvWriter);
        csvWriter.flush();
        csvWriter.close();
    }


    private void PoorBinDatInto(FileWriter csvWriter) throws IOException{
        for (int i = 0; i < getBinList().size(); i++){
            csvWriter.append(String.valueOf(getBinList().get(i).getId()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBinList().get(i).getData().getLength()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBinList().get(i).getData().getWidth()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBinList().get(i).getData().getHeight()));
            csvWriter.append("\n");
        }
    }



    private void PoorBoxDataInto(FileWriter csvWriter) throws IOException {
        for (int i = 0; i < getBoxList().size(); i ++){
            csvWriter.append(String.valueOf(getBoxList().get(i).getId()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBoxList().get(i).getX()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBoxList().get(i).getY()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBoxList().get(i).getZ()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBoxList().get(i).getLength()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBoxList().get(i).getWidth()));
            csvWriter.append(",");
            csvWriter.append(String.valueOf(getBoxList().get(i).getHeight()));
            csvWriter.append("\n");

        }
    }
    public ObservableList<Box> getBoxList() {
        return boxList;
    }

    public ObservableList<BinTree> getBinList() {
        return binList;
    }

    public String getPath() {
        return path;
    }


}
