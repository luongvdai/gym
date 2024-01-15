<template>
  <div class="infor" ref="infor">
    <div class="infor-right">
      <div class="infor-avatar">
        <img class="avatar" :src="urlImage" v-if="urlImage" />
        <img class="avatar" src="@\assets\photos\2.jpg" v-if="!urlImage" />
      </div>
    </div>
    <div class="infor-left">
      <div class="infor-input">Tên: {{ Infor.username }}</div>
      <div class="infor-input">Giới Tính: {{ formatGenderRecord(Infor.gender) }}</div>
      <div class="infor-input">Năm sinh: {{ formatUnixTime(Infor.dateOfBirth) }}</div>
    </div>
  </div>
</template>
<script>
import fireStorage from "@/plugins/fireStorage";
export default {
  props: {
    Infor: Object,
    top: {
      type: Number,
      default: 20,
    },
    left: {
      type: Number,
      default: 10,
    },
    position: {
      type: String,
      default: "right",
    },
  },
  created() {
    if (this.Infor) {
      (async () => {
        this.urlImage = await fireStorage.displayImage(this.Infor.avatar);
      })();
    }
  },
  methods:{
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
  },
  data() {
    return {
      urlImage: null,
    };
  },
};
</script>
<style scoped>
.infor {
  position: absolute;
  top: 0;
  right: -250px;
  width: 250px;
  height: 120px;
  background-color: #fff;
  border: 1px solid #bbbb;
  border-radius: 15px;
  z-index: 1200;
  display: flex;
}
.infor-right {
  width: 40%;
  height: 100%;
  display: flex;
  align-items: center;
}
.infor-avatar {
  width: 90%;
  height: 80%;
  margin-left: 10px;
  border: 1px solid black;
  border-radius: 8px;
}
.avatar {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}
.infor-left {
  width: 60%;
  height: 100%;
  padding-left: 10px;
}
.infor-input {
  max-width: 90%;
  height: 30%;
  display: flex;
  align-items: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>