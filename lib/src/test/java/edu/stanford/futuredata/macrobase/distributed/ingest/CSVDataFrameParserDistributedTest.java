package edu.stanford.futuredata.macrobase.distributed.ingest;

import edu.stanford.futuredata.macrobase.datamodel.Schema;
import edu.stanford.futuredata.macrobase.distributed.datamodel.DistributedDataFrame;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class CSVDataFrameParserDistributedTest {
    @Test
    public void testDistribution() throws Exception {
        Map<String, Schema.ColType> colTypes = new HashMap<>();
        colTypes.put("usage", Schema.ColType.DOUBLE);

        SparkConf conf = new SparkConf().setAppName("MacroBaseTest").setMaster("local");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        CSVDataFrameParserDistributed loader = new CSVDataFrameParserDistributed("src/test/resources/tiny.csv",
                Arrays.asList("usage", "location", "version"));
        loader.setColumnTypes(colTypes);

        DistributedDataFrame df = loader.load(sparkContext, 2);

        assertEquals(3, df.schema.getNumColumns());
        assertEquals("location", df.schema.getColumnName(2));

        List<Tuple2<String[], double[]>> collectedCSV = df.dataFrameRDD.collect();

        assertEquals(3, collectedCSV.size());

        assertEquals(2.0, collectedCSV.get(0)._2[0]);
        assertEquals("27", collectedCSV.get(0)._1[0]);
        assertEquals("USA", collectedCSV.get(0)._1[1]);
        assertEquals(3.1, collectedCSV.get(1)._2[0]);
        assertEquals("27", collectedCSV.get(1)._1[0]);
        assertEquals("CAN", collectedCSV.get(1)._1[1]);
        assertEquals(4.0, collectedCSV.get(2)._2[0]);
        assertEquals("28", collectedCSV.get(2)._1[0]);
        assertEquals("USA", collectedCSV.get(2)._1[1]);


        sparkContext.stop();
    }
}