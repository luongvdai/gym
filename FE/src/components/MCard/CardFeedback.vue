<template>
  <v-card class="mx-auto" width="200"  style="background-color: #fff8f5;border-radius: 20px" hover>
    <v-card-item class="content">
      <v-rating
        v-model="Feed.star"
        color="amber"
        density="compact"
        full-icon="ti ti-star-filled"
        empty-icon="ti ti-star"
        half-icon="ti ti-star-half-filled"
        half-increments
        readonly
        size="small"
      ></v-rating>
    </v-card-item>
    <v-card-text class="text-start">
      {{ Feed.content }}
    </v-card-text>
    <v-card-item>
      <div class="info">
        <img class="avatar" src="../../assets/photos/avatar.png" alt="" />
        <div class="text-start">
          <v-card-title class="name"> {{Feed.sender.username}} </v-card-title>
          <v-card-subtitle class="coursename">{{Feed.userCourse.ptCourse.course.title}}</v-card-subtitle>
        </div>
      </div>
    </v-card-item>
  </v-card>
</template>

<script>
import fireStorage from "@/plugins/fireStorage";
export default {
  name: "CardFeedback",
  props: {
    feedback: Object,
  },
  data() {
    return {
      Feed: {},
      urlImage: null,
    };
  },
  methods: {},
  created() {
    if(this.feedback){
      (async () => {
        this.urlImage = await fireStorage.displayImage(this.feedback.sender.avatar);
      })();
      this.Feed = this.feedback;
    }
  },
  beforeRouteLeave(to, from, next) {
    // Destroy trang cũ
    this.destroy();
    next();
  },
  destroy() {
    // Xóa tất cả dữ liệu
    this.Feed = {};
  },
};
</script>

<style scoped>
.info {
  display: flex !important;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.avatar{
  width: 50px;
  height: 50px;
}
.text-start{
  width: 75%;
    overflow: hidden;
}
.name{
  font-size: 16px !important;
}
.coursename{
  font-size: 13px !important;
}
.content{
  display: flex;
}
</style>
