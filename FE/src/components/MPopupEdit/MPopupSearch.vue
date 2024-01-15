<template>
  <div class="search-container">
    <div class="search-popup" v-if="isUser == true">
      <div
        class="search-item"
        v-for="index in searchItems"
        :key="index"
        @click="choice(index)"
      >
        <div class="item-infor name">
          <label class="item-label">{{ index.fullName }}</label>
        </div>
        <div class="item-infor">
          <label class="item-label">{{
            formatUnixTime(index.dateOfBirth)
          }}</label>
        </div>
        <div class="item-infor gender">
          <label class="item-label">{{
            formatGenderRecord(index.gender)
          }}</label>
        </div>
        <div class="item-infor name">
          <label class="item-label">{{ index.address }}</label>
        </div>
      </div>
    </div>
    <div class="search-popup" v-if="isUser == false">
      <div
        class="search-item"
        v-for="index in searchItems"
        :key="index"
        @click="choice(index)"
      >
        <div class="item-infor title">
          <label class="item-label">{{ index.title }}</label>
        </div>
        <div class="item-infor">
          <label class="item-label">{{ index.rangeProcess }} tháng</label>
        </div>
        <div class="item-infor">
          <label class="item-label">{{ index.type.name }}</label>
        </div>
        <div class="item-infor">
          <label class="item-label">{{ FormatVND(index.price) }}</label>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    searchItems: Array,
    isUser: Boolean,
  },
  created() {},

  methods: {
    choice(value) {
      this.$emit("choise-value", value);
    },
    /**
     * hàm format giới tính
     */
    formatGenderRecord(gender) {
      if (gender == true) {
        return "Nam";
      } else return "Nữ";
    },
    /**
     * hàm format UnixTime to date
     */
    formatUnixTime(unixTimestamp) {
      try {
        var date = new Date(unixTimestamp); // Multiply by 1000 to convert to milliseconds
        var day = date.getDate();
        var month = date.getMonth() + 1; // Months are zero-based
        var year = date.getFullYear();

        // Format the date as dd/mm/yyyy
        const formattedDate = `${day}/${month}/${year}`;

        return formattedDate;
      } catch (error) {
        console.log(error);
      }
    },
    FormatVND(price) {
      try {
        if (price) {
          price = price.toLocaleString("it-IT", {
            style: "currency",
            currency: "VND",
          });
          return price;
        } else {
          return "0 VNĐ";
        }
      } catch (error) {
        console.log(error);
      }
    },
  },
  data() {
    return {};
  },
};
</script>

<style scoped>
.search-container {
  top: 38px;
  left: 0;
  position: absolute;
}
.search-popup {
  position: relative;
  width: 600px;
  min-height: 50px;
  max-height: 200px;
  border: 1px solid #bbbb;
  border-radius: 8px;
  background-color: #fff;
  z-index: 300;
  overflow-x: hidden;
  overflow-y: auto;
}
.search-item {
  width: 100%;
  height: 50px;
  display: flex;
  cursor: pointer;
}
.item-infor {
  width: 20%;
  height: 100%;
  text-align: left;
  overflow: hidden;
  padding-left: 10px;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.item-infor.name {
  width: 30%;
}
.item-infor.title {
  width: 35%;
}
.item-infor.gender {
  padding-left: 30px;
}
.item-label {
  cursor: pointer;
}
.search-item:hover {
  color: #50b83c;
  background-color: #ebedf0;
}
</style>