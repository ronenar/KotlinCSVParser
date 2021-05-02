import csvparser.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaCall {

    static void showMissingCells(CSVFileData data) {
        List<Cell> cells = data.getMissingCells();
        String cellData = null;
        for (Cell cell : cells) {
            try {
                cellData = data.getCellValue(cell.row, cell.col);

            } catch (CSVFileException e) {
                e.printStackTrace();
            }
            System.out.println("Data for specific cell (" + cell.row + "," + cell.col + ") is " + cellData);
        }
    }

    static void demoFunctions(CSVFileData data) {

        int col = 7;

        try {
            System.out.println("Max for col " + col + " is : " + data.max(col));
            col = 3;
            System.out.println("Min for col " + col + " is : " + data.min(col));
            col = 4;
            System.out.println("Min for col " + col + " is : " + data.min(col));
            col = 3;
            System.out.println("Average for col " + col + " is : " + data.average(col));
            col = 16;
            System.out.println("Average for col " + col + " is : " + data.average(col));

        } catch (CSVFileException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        String path = "./demo/data.csv";
        String name = "data.csv";
        String streamFile ="./demo/data.csv";
        String compiledFilename = "./demo/data_fixed.csv";

        /*
        Map object specifies the column number in which a value might
        be missing and the strategy for filling the missing value
        (e.g. average, specific value, max, min etc..).
        */
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "min");
        map.put(2, "specific value");
        map.put(3, "average");
        map.put(4, "min");
        map.put(7, "min");

        CSVFile file = null;
        CSVFileData data = null;

        try {

            InputStream inputStream = new FileInputStream(streamFile);

            try {
                //file = new CSVFile(inputStream);
                file = new CSVFile(Paths.get(path));
                //file = new CSVFile(name);

                file.print();
                data = file.getCsvFileData();
                demoFunctions(data);
                file.compile(data);
                file.print();

            } catch (CSVFileException e) {

                try{
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();

                    Utils.printMissingCells(data);
                    showMissingCells(data);

                    file.compile(data, map, 100);

                    //alternativly using default value
                    //file.compileWithDefaultValue(data, map); // using default value 1 for missing cells

                    System.out.println("After compile:");
                    Utils.printMissingCells(data);// print there's no missing cells
                    file.print(); // show after fix missing cells
                    file.saveCompiledFile(compiledFilename);

                }catch (CSVFileException ex){
                    ex.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}