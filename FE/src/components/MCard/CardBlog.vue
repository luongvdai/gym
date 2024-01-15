<template>
  <div>
    <v-hover v-slot="{ isHovering, props }">
      <v-card
        :loading="loading"
        class="mx-auto my-12"
        max-width="374"
        v-bind="props"
      >
        <template v-slot:loader="{ isActive }">
          <v-progress-linear
            :active="isActive"
            color="deep-purple"
            height="4"
            indeterminate
          ></v-progress-linear>
        </template>
        <a class="blog" :href="`/blogspage/${card.id}`">
          <v-img
            v-if="!extractImageUrl(card.content)"
            cover
            height="250"
            src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
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
            </v-overlay></v-img
          >
          <v-img
            v-if="extractImageUrl(card.content)"
            cover
            height="250"
            :src="extractImageUrl(card.content)"
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
            </v-overlay></v-img
          >

          <v-card-item>
            <v-card-title>{{ card.title }}</v-card-title>

            <v-card-subtitle class="type">
              <span class="username me-1">{{ card.user.fullName }}</span>
              <div class="my-4 text-subtitle-1">
                {{ formatUnixTime(card.createdDate) }}
              </div>
            </v-card-subtitle>
          </v-card-item>

          <v-card-text>
            <div class="content">{{ extractPlainText(card.content) }}</div>
          </v-card-text>
        </a>
      </v-card>
    </v-hover>
  </div>
</template>

<script>
import fireStorage from "@/plugins/fireStorage";
export default {
  name: "CardBlog",
  components: {},
  props: {
    blog: Object,
  },
  data() {
    return {
      card: {},
      urlImage: null,
    };
  },
  methods: {
    extractImageUrl(content) {
      try {
        var imageUrl = null;
        const parser = new DOMParser();
        const doc = parser.parseFromString(content, "text/html");
        const imgElement = doc.querySelector("img");

        if (imgElement) {
          imageUrl = imgElement.getAttribute("src");
        }
        (async () => {
          imageUrl = await fireStorage.displayImage(imageUrl);
        })();
        return imageUrl;
      } catch (error) {
        console.log(error);
      }
    },

    extractPlainText(content) {
      try {
        var plainTextContent = null;
      const parser = new DOMParser();
      const doc = parser.parseFromString(content, "text/html");
      const paragraphElement = doc.querySelector("p");

      if (paragraphElement) {
        // Lấy nội dung thông thường từ phần tử <p>
        plainTextContent = paragraphElement.textContent;
      }
      return plainTextContent;
      } catch (error) {
        console.log(error);
      }
    },

    formatUnixTime(unixTimestamp) {
      try {
        const utcTime = new Date(unixTimestamp); // Nhân với 1000 để chuyển sang milliseconds
        const localTime = new Date(utcTime.getTime() + 7 * 60 * 60 * 1000); // Áp dụng múi giờ Việt Nam (UTC+7)

        const day = localTime.getDate();
        const month = localTime.getMonth() + 1; // Tháng được đếm từ 0
        const year = localTime.getFullYear();

        const formattedDateTime = `${day}/${month}/${year}`;

        return formattedDateTime; // Trả về ngày và giờ đã định dạng
      } catch (error) {
        console.log(error); // Xử lý lỗi nếu có
      }
    },
  },
  created() {
    if (this.blog) {
      (async () => {
        this.urlImage = await fireStorage.displayImage(this.blog.imageUrl);
      })();
      this.card = this.blog;
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
.blog {
  text-decoration: none;
}
.type {
  display: flex !important;
}
.username {
  display: flex;
  align-items: center;
}
.v-card-title {
  text-align: left;
}
.content {
  max-height: 100px;
  overflow: hidden;
  text-align: justify;
}
</style>
