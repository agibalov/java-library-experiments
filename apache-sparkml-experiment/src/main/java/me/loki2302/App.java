package me.loki2302;

import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // https://spark.apache.org/docs/latest/ml-classification-regression.html#linear-regression
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("JavaKMeansExample")
                .config("spark.master", "local")
                .getOrCreate();

        List<Row> rows = Arrays.asList(
                RowFactory.create(Vectors.dense(1.0), 10.0),
                RowFactory.create(Vectors.dense(2.0), 12.0),
                RowFactory.create(Vectors.dense(3.0), 14.0),
                RowFactory.create(Vectors.dense(4.0), 16.0),
                RowFactory.create(Vectors.dense(5.0), 18.0),
                RowFactory.create(Vectors.dense(6.0), 20.0),
                RowFactory.create(Vectors.dense(7.0), 22.0),
                RowFactory.create(Vectors.dense(8.0), 24.0),
                RowFactory.create(Vectors.dense(9.0), 26.0),
                RowFactory.create(Vectors.dense(10.0), 28.0)
        );
        StructType schema = new StructType(new StructField[] {
                new StructField("X", new VectorUDT(), false, Metadata.empty()),
                new StructField("y", DataTypes.DoubleType, false, Metadata.empty())
        });
        Dataset<Row> dataset = sparkSession.createDataFrame(rows, schema);
        System.out.printf("All data:\n");
        dataset.show();

        Dataset<Row>[] splits = dataset.randomSplit(new double[] {0.5, 0.5}, 1);
        System.out.printf("Training data:\n");
        Dataset<Row> trainingData = splits[0];
        trainingData.show();
        System.out.printf("Test data:\n");
        Dataset<Row> testData = splits[1];
        testData.show();

        LinearRegression linearRegression = new LinearRegression();
        linearRegression.setFeaturesCol("X");
        linearRegression.setLabelCol("y");
        LinearRegressionModel linearRegressionModel = linearRegression.fit(trainingData);
        System.out.printf("%s\n", linearRegressionModel.coefficients()); // [2]
        System.out.printf("%f\n", linearRegressionModel.intercept()); // 8

        LinearRegressionTrainingSummary trainingSummary = linearRegressionModel.summary();
        System.out.printf("rmse=%f\n", trainingSummary.rootMeanSquaredError()); // rmse=0
        System.out.printf("r2=%f\n", trainingSummary.r2()); // r2=1

        double prediction = linearRegressionModel.predict(Vectors.dense(11));
        System.out.printf("%f\n", prediction); // 30

        Dataset<Row> predictions = linearRegressionModel.transform(testData);
        predictions.show();
        RegressionEvaluator regressionEvaluator = new RegressionEvaluator();
        regressionEvaluator.setLabelCol("y");
        regressionEvaluator.setPredictionCol("prediction");
        regressionEvaluator.setMetricName("rmse");
        double rmse = regressionEvaluator.evaluate(predictions);
        System.out.printf("rmse=%f\n", rmse);

        sparkSession.stop();
    }
}
