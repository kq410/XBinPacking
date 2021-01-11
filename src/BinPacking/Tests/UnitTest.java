package BinPacking.Tests;

import BinPacking.CSVwriter.CSVwriter;
import BinPacking.Data.Logic.BinSpace.Dimensions;
import BinPacking.Data.Logic.BinTree.BinTree;
import BinPacking.Data.Logic.Box.Box;
import BinPacking.Data.Logic.InputData.InputData;
import BinPacking.Logic.Packer.BinPacker;
import BinPacking.Logic.PackingStrategy.PackingStrategyFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * Created by Xsignati on 21.03.2017.
 * TEST VERSION. REAL UNIT TESTS TO DO.
 */
public class UnitTest {
    @Test
    public void test() {
        //source lists
        ObservableList<Box> boxList = FXCollections.observableArrayList();
        ObservableList<BinTree> binList = FXCollections.observableArrayList();
        ObservableList<Box> testBoxList = FXCollections.observableArrayList();

        double binLength;
        double binWidth;
        double binHeight;

        //Read data from txt. First line = bin size. Other lines = Box [l w h x y z]
        try {
            FileInputStream fi = new FileInputStream("src/BinPacking/Tests/input.txt");
            InputStreamReader isr = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(isr);


            String line = br.readLine();

            String[] values = line.split(" ");
            binLength = Double.parseDouble(values[0]);
            binWidth = Double.parseDouble(values[1]);
            binHeight = Double.parseDouble(values[2]);

            while((line = br.readLine()) != null){
                values = line.split(" ");
                double boxLength = Double.parseDouble(values[0]);
                double boxWidth = Double.parseDouble(values[1]);
                double boxHeight = Double.parseDouble(values[2]);
                double boxX = Double.parseDouble(values[3]);
                double boxY = Double.parseDouble(values[4]);
                double boxZ = Double.parseDouble(values[5]);
                int boxNum = Integer.parseInt(values[6]);
                for (int i = 0; i < boxNum; i++){
                    boxList.add(new Box(new Dimensions(boxLength, boxWidth, boxHeight)));
                    Box testBox = new Box(new Dimensions(boxLength, boxWidth, boxHeight));
                    testBox.setCoordinates(boxX, boxY, boxZ);
                    testBoxList.add(testBox);
                }
            }

            InputData inputData = new InputData(binLength, binWidth,binHeight, binList, PackingStrategyFactory.getPS("BestFit"), boxList);
            BinPacker loader = new BinPacker();
            loader.pack(inputData);

            br.close();
            CSVwriter writer = new CSVwriter(boxList, binList, "src/BinPacking/Tests");
            writer.WriteAllData();


        }
        catch(FileNotFoundException e)
            {e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}


//        assertThat(boxList, is(testBoxList));
    }
}
