<template>
  <div class="hello" id="chartdiv" ref="LineChart"></div>
</template>

<script>
import {
  am5,
  am5xy,
  am5themes_Animated,
  am5plugins_exporting,
} from "@/helpers/amchart";
export default {
  name: "LineChart",
  props: {
    arrRevenue: [],
  },
  data() {
    return {};
  },
  created() {},
  mounted() {
    const dom = document.getElementById("chartdiv");
    if (dom) {
      let root = am5.Root.new(this.$refs.LineChart);

      root.setThemes([am5themes_Animated.new(root)]);

      let chart = root.container.children.push(
        am5xy.XYChart.new(root, {
          panX: false,
          panY: false,
          wheelX: "panX",
          wheelY: "zoomX",
          layout: root.verticalLayout,
        })
      );

      // Define data
      let data = this.arrRevenue;
      // Create axes
      // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
      let xRenderer = am5xy.AxisRendererX.new(root, {
        cellStartLocation: 0.1,
        cellEndLocation: 0.9,
        minGridDistance: 50,
      });
      // Create Y-axis
      let yAxis = chart.yAxes.push(
        am5xy.ValueAxis.new(root, {
          renderer: am5xy.AxisRendererY.new(root, {
            strokeOpacity: 0.1,
          }),
        })
      );

      // Create X-Axis
      let xAxis = chart.xAxes.push(
        am5xy.CategoryAxis.new(root, {
          categoryField: "month",
          renderer: xRenderer,
          tooltip: am5.Tooltip.new(root, {}),
        })
      );
      xRenderer.grid.template.setAll({
        location: 1,
      });

      xRenderer.labels.template.setAll({
        multiLocation: 0.5,
      });
      xAxis.data.setAll(data);

      // Create series
      let series = chart.series.push(
        am5xy.ColumnSeries.new(root, {
          xAxis: xAxis,
          yAxis: yAxis,
          valueYField: "value",
          categoryXField: "month",
        })
      );
      series.columns.template.setAll({
        tooltipText: "{categoryX}: {valueY}",
        width: am5.percent(90),
        tooltipY: 0,
        strokeOpacity: 0,
        templateField: "columnSettings",
      });
      series.data.setAll(data);
      // Add export menu
      am5plugins_exporting.Exporting.new(root, {
        menu: am5plugins_exporting.ExportingMenu.new(root, {}),
      });
      // Make stuff animate on load
      // https://www.amcharts.com/docs/v5/concepts/animations/
      this.root = root;
      this.chart = chart;
      this.xRenderer = xRenderer;
      this.yAxis = yAxis;
      this.xAxis = xAxis;
      this.series = series;

      series.appear();
      chart.appear(1000, 100);
    }
  },
  beforeUnmount() {
    if (this.root) {
      this.root.dispose();
    }
  },

  watch: {
    arrRevenue() {
      if (this.root) {
        this.data = this.arrRevenue;
        this.xAxis.data.setAll(this.data);
        this.series.data.setAll(this.data);
      }
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  width: 100%;
  height: 500px;
}
</style>
