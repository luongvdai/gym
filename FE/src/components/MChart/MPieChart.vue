<template>
  <div class="">
    <div class="d-flex my-5">
      <v-card width="400" class="card-1 text-start"
        ><v-card-item>
          <v-card-title>Doanh thu theo khóa tập</v-card-title>
        </v-card-item>
        <v-card-text
          ><h2>{{ FormatVND(total) }}</h2></v-card-text
        >
        <i class="ti ti-pig-money"></i>
      </v-card>
      <div class="filter">
        <div class="input-container my-2">
          <label for="date-from">Ngày bắt đầu</label>
          <input type="date" class="date-field" v-model="fromDate" />
        </div>
        <div class="input-container">
          <label for="date-from">Ngày kết thúc</label>
          <input type="date" class="date-field" v-model="toDate" />
        </div>
      </div>
    </div>
    <div class="d-flex">
      <div class="hello" id="chartdiv" ref="PieChart"></div>
    </div>
    <MLoading v-if="showLoading" />
  </div>
</template>

<script>
import { am5, am5themes_Animated, am5percent } from "@/helpers/amchart";
import fetchOptions from "@/api/base/fetchOptions";
import apiConfig from "@/api/config/apiConfig";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import MLoading from "@/components/MLoading/MLoading.vue";

export default {
  name: "PieChart",
  components: {
    MLoading,
  },
  props: {},
  data() {
    return {
      resultPie: [],
      fromDate: null,
      toDate: null,
      total: 0,
      showLoading: false,
    };
  },
  created() {
    (async () => {
      await this.loadCourses();
      await this.loadTotal();
    })();
  },
  mounted() {
    let root = am5.Root.new(this.$refs.PieChart);

    root.setThemes([am5themes_Animated.new(root)]);

    let chart = root.container.children.push(
      am5percent.PieChart.new(root, {
        layout: root.verticalLayout,
      })
    );
    let data = this.resultPie;
    // Create series
    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series
    let series = chart.series.push(
      am5percent.PieSeries.new(root, {
        valueField: "value",
        categoryField: "category",
      })
    );

    // Set data
    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data
    series.data.setAll(data);

    // Create legend
    // https://www.amcharts.com/docs/v5/charts/percent-charts/legend-percent-series/
    var legend = chart.children.push(
      am5.Legend.new(root, {
        centerX: am5.percent(50),
        x: am5.percent(50),
        marginTop: 15,
        marginBottom: 15,
      })
    );
    legend.data.setAll(series.dataItems);
    this.root = root;
    this.series = series;
    this.legend = legend;
    chart.appear(1000, 100);

    // Play initial series animation
    // https://www.amcharts.com/docs/v5/concepts/animations/#Animation_of_series
    series.appear(1000, 100);
  },

  beforeUnmount() {
    if (this.root) {
      this.root.dispose();
    }
  },
  methods: {
    FormatVND(amount) {
      try {
        // Định dạng số thành chuỗi
        let formattedValue = amount.toString();

        // Chia thành các phần mỗi 3 số và thêm dấu chấm phân cách
        formattedValue = formattedValue.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

        // Thêm đơn vị tiền tệ
        formattedValue += " VNĐ";

        return formattedValue;
      } catch (error) {
        console.log("Lỗi format VNĐ");
      }
    },
    async loadCourses() {
      try {
        this.showLoading = true;
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
        await fetch(
          `${apiConfig}/stats/top-course?${filter}`,
          await fetchOptions("GET")
        )
          .then((response) => response.json().catch(() => response))
          .then(async (response) => {
            this.resultPie = [];
            response.forEach((item) => {
              this.resultPie.push({
                value: item.total,
                category: item.courseName,
              });
            });
            this.series.data.setAll(this.resultPie);
            this.legend.data.setAll(this.series.dataItems);
            this.showLoading = false;
          })
          .catch((error) => {
            console.log(error);
            this.showLoading = false;
          });
      } catch (error) {
        console.log(error);
        this.showLoading = false;
      }
    },
    async loadTotal() {
      try {
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
        await fetch(
          `${apiConfig}/stats/revenue?${filter}`,
          await fetchOptions("GET")
        )
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            this.total = response;
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
    getBranch() {
      if (this.root) {
        this.loadCourses();
        this.loadTotal();
      }
    },
    fromDate() {
      if (this.root) {
        this.loadCourses();
        this.loadTotal();
      }
    },
    toDate() {
      if (this.root) {
        this.loadCourses();
        this.loadTotal();
      }
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
.hello {
  width: 100%;
  height: 500px;
}
.card-1 {
  border-left: 5px solid #2ca01c;
}
.ti {
  position: absolute;
  top: 5%;
  right: 5%;
  font-size: -webkit-xxx-large;
}
.filter {
  position: absolute;
  left: 70%;
  top: 5%;
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
