package com.ntpclient.ui;

import com.ntpclient.datamodel.DataModel;
import com.ntpclient.datamodel.HistoryDataGroup;
import com.ntpclient.datamodel.Observer;
import javafx.scene.layout.StackPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.MouseHandlerFX;
import org.jfree.chart.fx.interaction.PanHandlerFX;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.util.Date;

public class HistoryChart extends StackPane implements Observer {
    private TimeSeriesCollection timeDataset = new TimeSeriesCollection();
    private JFreeChart chart;
    private double windowSizeMillis = 1000.0 * 60.0 * 10.0;

    public HistoryChart() {
        super();
        DataModel.getInstance().getHistoryDataGroup().addObserver(this);

        this.chart = ChartFactory.createTimeSeriesChart(
                null,
                "time",
                "offset",
                timeDataset,
                true,
                false,
                false);

        ChartViewer chartViewer = new ChartViewer(chart);
        // remove default pan handler
        MouseHandlerFX mouseHandlerFX = chartViewer.getCanvas().getMouseHandler("pan");
        chartViewer.getCanvas().removeMouseHandler(mouseHandlerFX);
        // add pan handler without keys
        PanHandlerFX panHandlerFX = new PanHandlerFX("pan", false, false, false, false);
        chartViewer.getCanvas().addMouseHandler(panHandlerFX);
        // set zoomable
        chartViewer.getCanvas().setDomainZoomable(true);
        chartViewer.getCanvas().setRangeZoomable(true);
        // add to pane
        this.getChildren().add(chartViewer);
        // set pannable on both axes
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
    }

    @Override
    public void onNotify() {
        TimeSeries roundTripTimeSeries = new TimeSeries("round trip time");
        TimeSeries offsetTimeSeries = new TimeSeries("offset time");

        Date date = new Date();
        for (HistoryDataGroup.HistoryEntry historyEntry : DataModel.getInstance().getHistoryDataGroup().getHistoryEntries()) {
            long currentTime = (long) (historyEntry.getCurrentTime() - 2208988800L) * 1000L;
            date.setTime(currentTime);
            Second second = new Second(date);
            offsetTimeSeries.addOrUpdate(second, historyEntry.getTimeOffset());
            roundTripTimeSeries.addOrUpdate(second, historyEntry.getRoundTripDelay());
        }

        this.timeDataset.removeAllSeries();
        this.timeDataset.addSeries(offsetTimeSeries);
        this.timeDataset.addSeries(roundTripTimeSeries);

        final double MinWindowSizeMillis = 1000.0 * 60.0 * 10.0;
        // update axes range
        Range domainRange = timeDataset.getDomainBounds(true);
        windowSizeMillis = Math.max(
                Math.min(windowSizeMillis, domainRange.getUpperBound() - domainRange.getLowerBound()),
                MinWindowSizeMillis
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setRange(new Range(domainRange.getUpperBound() - windowSizeMillis, domainRange.getUpperBound()));
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setAutoRange(true);

        chart.fireChartChanged();
    }
}