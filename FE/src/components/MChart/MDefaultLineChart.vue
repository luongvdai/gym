<template>
  <div class="">
    <div class="d-flex my-5">
      <v-card width="400" class="card-1 text-start"
        ><v-card-item>
          <v-card-title>Số lượng</v-card-title>
        </v-card-item>

        <v-card-text
          ><h1>{{ result.total }}</h1></v-card-text
        >
        <i class="ti ti-user-plus"></i>
      </v-card>
      <v-card width="400" class="card-2 text-start"
        ><v-card-item>
          <v-card-title>Nam</v-card-title>
        </v-card-item>

        <v-card-text
          ><h1>{{ result.totalMale }}</h1></v-card-text
        >
        <i class="ti ti-gender-male"></i>
      </v-card>
      <v-card width="400" class="card-3 text-start"
        ><v-card-item>
          <v-card-title>Nữ</v-card-title>
        </v-card-item>

        <v-card-text
          ><h1>{{ result.totalFemale }}</h1></v-card-text
        >
        <i class="ti ti-gender-female"></i>
      </v-card>
      <div class="filter">
        <div class="input-container my-2" id="date-picker-container">
          <label for="date-from">Ngày bắt đầu</label>
          <input type="date" class="date-field" v-model="fromDate" />
        </div>
        <div class="input-container" id="date-picker-container">
          <label for="date-from">Ngày kết thúc</label>
          <input type="date" class="date-field" v-model="toDate" />
        </div>
      </div>
    </div>
    <div class="hello" id="chartdiv" ref="DefaultLineChart"></div>
  </div>
</template>

<script>
import {
  am5,
  am5xy,
  am5themes_Animated,
  am5plugins_exporting,
} from "@/helpers/amchart";
import fetchOptions from "@/api/base/fetchOptions";
import apiConfig from "@/api/config/apiConfig";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
export default {
  name: "LineChart",
  components: {},
  data() {
    return {
      result: {
        total: 0,
        totalMale: 0,
        totalFemale: 0,
      },
      fromDate: null,
      toDate: null,
      isAdmin: null,
      allBranch: true,
    };
  },
  created() {
    (async () => {
      this.isAdmin =
        JSON.parse(localStorage.getItem("userData"))["role"] == "ADMIN"
          ? true
          : false;
      await this.loadData();
    })();
  },
  async mounted() {
    let root = am5.Root.new(this.$refs.DefaultLineChart);
    root.setThemes([am5themes_Animated.new(root)]);

    let chart = root.container.children.push(
      am5xy.XYChart.new(root, {
        panY: false,
        layout: root.verticalLayout,
      })
    );
    // Define data
    let data = [
      {
        category: "Người dùng mới",
        value1: this.result.totalMale,
        value2: this.result.totalFemale,
      },
    ];
    // Create Y-axis
    let yAxis = chart.yAxes.push(
      am5xy.ValueAxis.new(root, {
        renderer: am5xy.AxisRendererY.new(root, {}),
      })
    );
    // Create X-Axis
    let xAxis = chart.xAxes.push(
      am5xy.CategoryAxis.new(root, {
        renderer: am5xy.AxisRendererX.new(root, {}),
        categoryField: "category",
      })
    );
    xAxis.data.setAll(data);

    // Create series
    let series1 = chart.series.push(
      am5xy.ColumnSeries.new(root, {
        name: "Nam",
        xAxis: xAxis,
        yAxis: yAxis,
        valueYField: "value1",
        categoryXField: "category",
      })
    );
    series1.data.setAll(data);

    let series2 = chart.series.push(
      am5xy.ColumnSeries.new(root, {
        name: "Nữ",
        xAxis: xAxis,
        yAxis: yAxis,
        valueYField: "value2",
        categoryXField: "category",
      })
    );
    series2.data.setAll(data);

    // Add legend
    let legend = chart.children.push(am5.Legend.new(root, {}));
    legend.data.setAll(chart.series.values);

    // Add cursor
    chart.set("cursor", am5xy.XYCursor.new(root, {}));
    series1.appear(1000);
    series2.appear(1000);
    chart.appear(1000, 100);
    am5plugins_exporting.Exporting.new(root, {
      menu: am5plugins_exporting.ExportingMenu.new(root, {}),
    });
    this.root = root;
    this.chart = chart;
    this.yAxis = yAxis;
    this.xAxis = xAxis;
    this.series1 = series1;
    this.series2 = series2;
    this.legend = legend;
  },
  beforeUnmount() {
    if (this.root) {
      this.root.dispose();
    }
  },
  methods: {
    async loadData() {
      let filter = "";
      filter += `branchId=${this.getBranch}`;
      if (this.fromDate) {
        const date = new Date(this.fromDate);
        const fromTimeStamp = date.getTime();
        filter += `&fromDate=${fromTimeStamp}`;
      }
      if (this.toDate) {
        const date = new Date(this.toDate);
        const toTimeStamp = date.getTime();
        filter += `&toDate=${toTimeStamp}`;
      }
      try {
        await fetch(
          `${apiConfig}/stats/new-user?${filter}`,
          await fetchOptions("GET")
        )
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            this.result = response;
            this.data = [
              {
                category: "Người dùng mới",
                value1: response.totalMale,
                value2: response.totalFemale,
              },
            ];
            this.xAxis.data.setAll(this.data);
            this.series1.data.setAll(this.data);
            this.series2.data.setAll(this.data);
            this.legend.data.setAll(this.chart.series.values);
          })
          .catch((error) => {
            console.log(error);
          });
      } catch (error) {
        console.log(error);
      }
    },
  },
  watch: {
    fromDate() {
      this.loadData();
    },
    toDate() {
      this.loadData();
    },
    getBranch() {
      this.loadData();
    },
  },
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_SELECTED,
    }),
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  width: 100%;
  height: 500px;
}
.d-flex {
  gap: 20px;
}
.card-1 {
  border-left: 5px solid #2ca01c;
}
.card-2 {
  border-left: 5px solid #67b7dc;
  position: relative;
}
.ti {
  position: absolute;
  top: 30%;
  right: 10%;
  font-size: -webkit-xxx-large;
}
.ti-user-plus {
  color: #2ca01c;
}
.ti-gender-male {
  color: #67b7dc;
}
.ti-gender-female {
  color: #6794dc;
}
.card-3 {
  border-left: 5px solid #6794dc;
}
.filter {
  height: 40px;
  gap: 10px;
}
/* INPUT CONTAINER  */
.input-container {
  display: flex;
  flex-direction: column;
  width: auto;
  height: auto;
  min-width: 217px;
  height: 53px;
  padding: 0px 10px 10px 10px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  text-align: start;
}
.input-container label {
  position: relative;
  width: 100%;
  font-family: "Segoe UI";
  font-weight: 600;
  font-size: 11px;
  letter-spacing: 0.1em;
  line-height: 20px;
  color: #09f;
  margin-left: 2px;
  text-transform: uppercase;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.input-container input {
  position: relative;
  align-items: center;
  width: auto;
  height: auto;
  font-family: "Lekton", Arial, sans-serif;
  font-size: 16px;
  letter-spacing: 0.05em;
  line-height: 21px;
  text-transform: uppercase;
  margin-left: 0px;
}
</style>
