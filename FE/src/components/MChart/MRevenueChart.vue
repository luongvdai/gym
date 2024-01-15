<template>
  <div class="">
    <div class="d-flex my-5">
      <v-card width="400" class="card-1 text-start"
        ><v-card-item>
          <v-card-title>Doanh thu</v-card-title>
        </v-card-item>

        <v-card-text
          ><h2>{{ FormatVND(revenue) }}</h2></v-card-text
        >
        <i class="ti ti-presentation-analytics"></i>
      </v-card>
      <MCombobox
        class="filter"
        :inputLabel="'Năm'"
        :Drstyle="'height: 35px; width: 10%;'"
        :DropboxItem="filter"
        @combobox-value="getYear"
        :value="'key'"
        :label="'value'"
        :maxlength="10"
        ref="combobox"
      />
    </div>
    <div class="d-flex">
      <MLineChart :arrRevenue="resultLine" />
    </div>
    <MLoading v-if="showLoading" />
  </div>
</template>

<script>
import fetchOptions from "@/api/base/fetchOptions";
import apiConfig from "@/api/config/apiConfig";
import MLineChart from "@/components/MChart/MLineChart.vue";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import MLoading from "@/components/MLoading/MLoading.vue";

export default {
  name: "RevenueChart",
  components: {
    MLineChart,
    MCombobox,
    MLoading,
  },
  data() {
    return {
      resultLine: [],
      year: 0,
      revenue: 0,
      filter: [],
      showLoading: false,
    };
  },
  created() {
    const currentYear = new Date().getFullYear();
    for (let i = currentYear; i > 2019; i--) {
      this.filter.push({
        key: i,
        value: i,
      });
    }
    (async () => {
      await this.loadData();
    })();
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
    getYear(year) {
      this.year = year;
    },

    async loadData() {
      try {
        this.showLoading = true;
        let keyword = "";
        if (this.year != 0) {
          keyword += `?year=${this.year}`;
        } else {
          const currentYear = new Date().getFullYear();
          keyword += `?year=${currentYear}`;
        }
        keyword += `&branchId=${this.getBranch}`;
        await fetch(
          `${apiConfig}/stats/revenue/by-year${keyword}`,
          await fetchOptions("GET")
        )
          .then((response) => response.json().catch(() => response))
          .then(async (response) => {
            this.revenue = 0;
            const arrData = [];
            for (let i = 1; i <= 12; i++) {
              this.revenue += response[i];
              arrData.push({
                month: `Tháng ${i}`,
                value: response[i],
              });
            }
            this.resultLine = arrData;
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
  },
  beforeUnmount() {
    if (this.root) {
      this.root.dispose();
    }
  },
  watch: {
    year() {
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
  left: 90%;
  top: 10%;
}
</style>
