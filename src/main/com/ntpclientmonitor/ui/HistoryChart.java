package com.ntpclientmonitor.ui;

import com.ntpclientmonitor.datamodel.DataModel;
import com.ntpclientmonitor.datamodel.HistoryData;
import com.ntpclientmonitor.datamodel.Observer;
import javafx.scene.layout.StackPane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.MouseHandlerFX;
import org.jfree.chart.fx.interaction.PanHandlerFX;
import org.jfree.chart.fx.interaction.ZoomHandlerFX;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.util.Date;

class HistoryChart extends StackPane implements Observer {
    private TimeSeriesCollection offsetDataset = new TimeSeriesCollection();
    private TimeSeriesCollection frequencyDataset = new TimeSeriesCollection();
    private JFreeChart chart;

    HistoryChart() {
        super();
        DataModel.getInstance().getHistoryDataGroup().addObserver(this);

        XYPlot plot = new XYPlot();
        plot.setDataset(0, offsetDataset);
        plot.setDataset(1, frequencyDataset);

        XYLineAndShapeRenderer xyLineAndShapeRenderer0 = new XYLineAndShapeRenderer();
        xyLineAndShapeRenderer0.setDrawSeriesLineAsPath(true);
        xyLineAndShapeRenderer0.setSeriesPaint(0, Color.DARK_GRAY);
        xyLineAndShapeRenderer0.setSeriesPaint(1, Color.LIGHT_GRAY);
        xyLineAndShapeRenderer0.setSeriesShapesVisible(0, false);
        xyLineAndShapeRenderer0.setSeriesShapesVisible(1, false);
        plot.setRenderer(0, xyLineAndShapeRenderer0);

        XYLineAndShapeRenderer xyLineAndShapeRenderer1 = new XYLineAndShapeRenderer();
        xyLineAndShapeRenderer1.setDrawSeriesLineAsPath(true);
        xyLineAndShapeRenderer1.setSeriesPaint(0, Color.RED);
        xyLineAndShapeRenderer1.setSeriesPaint(1, Color.PINK);
        xyLineAndShapeRenderer1.setSeriesShapesVisible(0, false);
        xyLineAndShapeRenderer1.setSeriesShapesVisible(1, false);
        plot.setRenderer(1, xyLineAndShapeRenderer1);

        plot.setRangeAxis(0, new NumberAxis("seconds"));
        plot.setRangeAxis(1, new NumberAxis("ppm"));
        plot.setDomainAxis(new DateAxis("time"));

        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        // set pannable on both axes
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        this.chart = new JFreeChart("hold ctrl key to define zoom box with mouse",
                new Font("Courier", Font.PLAIN, 12), plot, true);
        ChartViewer chartViewer = new ChartViewer(chart, false);

        // remove default pan handler
        MouseHandlerFX mouseHandlerFX = chartViewer.getCanvas().getMouseHandler("pan");
        chartViewer.getCanvas().removeMouseHandler(mouseHandlerFX);
        MouseHandlerFX scrollHandlerFX = chartViewer.getCanvas().getMouseHandler("scroll");
        chartViewer.getCanvas().removeAuxiliaryMouseHandler(scrollHandlerFX);
        MouseHandlerFX mouseZoomHandlerFX = chartViewer.getCanvas().getMouseHandler("zoom");
        chartViewer.getCanvas().removeMouseHandler(mouseZoomHandlerFX);

        // add pan handler without keys
        PanHandlerFX panHandlerFX = new PanHandlerFX("pan", false, false, false, false);
        chartViewer.getCanvas().addMouseHandler(panHandlerFX);
        // set zoomable
        ZoomHandlerFX zoomHandlerFX = new ZoomHandlerFX("zoom", chartViewer, false, true, false, false);
        chartViewer.getCanvas().addMouseHandler(zoomHandlerFX);
        // add to pane
        this.getChildren().add(chartViewer);

        setFocusTraversable(false);
    }

    @Override
    public void onNotify() {
        TimeSeries offsetTimeSeries = new TimeSeries("offset (sec)");
        TimeSeries frequencyOffsetPpmTimeSeries = new TimeSeries("offset (ppm)");
        TimeSeries rmsJitterSeries = new TimeSeries("rms jitter (sec)");
        TimeSeries allanDeviationSeries = new TimeSeries("allan deviation (ppm)");

        Date date = new Date();
        for (HistoryData historyData : DataModel.getInstance().getHistoryDataGroup().getHistoryData()) {
            Second second = new Second(historyData.getDate());
            offsetTimeSeries.addOrUpdate(second, historyData.getTimeOffset());
            frequencyOffsetPpmTimeSeries.addOrUpdate(second, historyData.getFrequencyOffsetPpm());
            rmsJitterSeries.addOrUpdate(second, historyData.getRmsJitter());
            allanDeviationSeries.addOrUpdate(second, historyData.getAllanDeviation());
        }

        offsetDataset.removeAllSeries();
        offsetDataset.addSeries(offsetTimeSeries);
        offsetDataset.addSeries(rmsJitterSeries);
        frequencyDataset.removeAllSeries();
        frequencyDataset.addSeries(frequencyOffsetPpmTimeSeries);
        frequencyDataset.addSeries(allanDeviationSeries);

        if (DataModel.getInstance().getHistoryDataGroup().isNewSelection()) {
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.getDomainAxis().setAutoRange(false);
            plot.getDomainAxis().setAutoRange(true);

            plot.getRangeAxis(0).setAutoRange(false);
            plot.getRangeAxis(0).setAutoRange(true);
            plot.getRangeAxis(1).setAutoRange(false);
            plot.getRangeAxis(1).setAutoRange(true);
        }
    }
}