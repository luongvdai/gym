<template>
  <div class="container-dashboard">
    <div class="container-data">
      <v-tabs v-model="tab" bg-color="transparent" color="basil" grow>
        <v-tab v-for="item in items" :key="item" :value="item.value">
          {{ item.name }}
        </v-tab>
      </v-tabs>
      <v-window v-model="tab">
        <v-window-item :value="1">
          <MDefaultLineChart />
        </v-window-item>
        <v-window-item :value="2">
          <MPieChart />
        </v-window-item>
        <v-window-item :value="3">
          <MRevenueChart />
        </v-window-item>
      </v-window>
    </div>
  </div>
</template>

<script>
import MRevenueChart from "@/components/MChart/MRevenueChart.vue";
import MDefaultLineChart from "@/components/MChart/MDefaultLineChart.vue";
import MPieChart from "@/components/MChart/MPieChart.vue";

export default {
  name: "Admin-Dashboard",
  components: {
    MRevenueChart,
    MDefaultLineChart,
    MPieChart,
  },
  methods: {},
  created() {},
  data() {
    return {
      tab: 1,
      items: [
        { value: 1, name: "Tài khoản đăng ký mới" },
        { value: 2, name: "Doanh thu" },
        { value: 3, name: "Thống kê theo năm" },
      ],
    };
  },
};
</script>

<style scoped>
.text-basil {
  color: #2ca01c !important;
}
.container-dashboard {
  width: 100%;
  height: 100%;
  margin: auto auto;
}
.container-data {
  position: relative;
  margin: 0 3%;
  padding: 0 3%;
  border: 1px solid rgb(216, 211, 211);
  border-radius: 13px;
  background: #fff;
  height: 100%;
}
</style>
