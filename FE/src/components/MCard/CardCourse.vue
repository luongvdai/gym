<template>
  <div>
    <v-hover v-slot="{ isHovering, props }">
      <v-card class="card mx-auto" min-width="320" v-bind="props">
        <div v-if="isNewCourse" class="new-title">
          <span>New</span>
        </div>
        <a :href="`/courses/${card.id}`">
          <v-img
            v-if="!urlImage"
            cover
            height="200"
            src="https://citigym.com.vn/storage/uploads/thumbnail-3-2.jpg"
          >
            <v-overlay
              :model-value="isHovering"
              contained
              scrim="#1f1f1f"
              class="align-center justify-center"
            >
              <v-btn variant="outlined" color="#f1f1f1" block rounded="xl"
                >Xem chi tiết</v-btn
              >
            </v-overlay>
          </v-img>
          <v-img v-if="urlImage" cover height="200" :src="urlImage">
            <v-overlay
              :model-value="isHovering"
              contained
              scrim="#1f1f1f"
              class="align-center justify-center"
            >
              <v-btn variant="outlined" color="#f1f1f1" block rounded="xl"
                >Xem chi tiết</v-btn
              >
            </v-overlay>
          </v-img>
        </a>
        <v-card-item class="text-start">
          <v-card-title class="title">{{ card.title }}</v-card-title>
        </v-card-item>
        <v-card-item class="text-start">
          <v-card-subtitle>
            <span>Thời lượng: {{ card.rangeProcess }} tháng</span>
          </v-card-subtitle>
          <div>
            <span class="font-weight-bold">{{ FormatVND(card.price) }}</span>
          </div>
        </v-card-item>
      </v-card>
    </v-hover>
  </div>
</template>

<script>
import fireStorage from "@/plugins/fireStorage";
export default {
  name: "CardCourse",
  components: {},
  props: {
    course: Object,
  },
  data() {
    return {
      card: {},
      urlImage: null,
    };
  },
  computed: {
    isNewCourse() {
      try {
        if (this.course) {
        const currentDate = new Date();
        const createdDate = new Date(this.course.createdDate);
        const daysDifference =
          (currentDate - createdDate) / (1000 * 60 * 60 * 24);
        const numberOfDays = 30;
        return daysDifference <= numberOfDays;
      }
      return false;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        return false;
      }
    },
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
        console.log(error);
      }
    },
  },
  created() {
    if (this.course) {
      (async () => {
        this.urlImage = await fireStorage.displayImage(this.course.imageUrl);
      })();
      this.card = this.course;
    }
  },
  beforeRouteLeave(to, from, next) {
    // Destroy trang cũ
    this.destroy();
    next();
  },
  destroy() {
    // Xóa tất cả dữ liệu
    this.card = {};
  },
};
</script>

<style scoped>
.title {
  max-width: 280px;
}
.card {
  position: relative;
}
.discount-title {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: #e9485a;
  color: #fff;
  width: fit-content;
  z-index: 1;
  padding: 1px 3px;
  border-radius: 2px;
}
.new-title {
  position: absolute;
  top: 5px;
  left: 5px;
  background-color: #e9485a;
  color: #fff;
  width: fit-content;
  z-index: 1;
  padding: 1px 5px;
  border-radius: 2px;
}
a {
  text-decoration: none;
}
</style>
