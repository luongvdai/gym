<template>
  <div class="blog-container">
    <div class="w3-display-container w3-animate-opacity">
      <v-carousel
        height="600"
        interval="3000"
        cycle
        hide-delimiters
        show-arrows="hover"
        prev-icon="ti ti-chevron-left"
        next-icon="ti ti-chevron-right"
        v-if="!isType"
      >
        <v-carousel-item v-for="i in listBlog" :key="i">
          <v-sheet :color="colors[i]" height="100%">
            <div class="sheet">
              <router-link
                :to="`/blogspage/${i.id}`"
                class="sider-img bg-orange"
              >
                <img
                  v-if="!extractImageUrl(i.content)"
                  class="zoom-in-image w-100 h-100"
                  src="@\assets\photos\banner1.jpg"
                />
                <img
                  v-if="extractImageUrl(i.content)"
                  class="zoom-in-image w-100 h-100"
                  :src="extractImageUrl(i.content)"
                />
                <v-card
                  :elevation="0"
                  class="card"
                  position="absolute"
                  v-bind="props"
                  max-width="100%"
                >
                  <v-card-item class="text-start banner-title">
                    <v-card-title class="title"
                      ><h3>{{ i.title }}</h3>
                    </v-card-title>
                  </v-card-item>
                  <v-card-item class="text-justify banner-des">
                    <p>
                      {{ extractPlainText(i.content) }}
                    </p>
                  </v-card-item>
                </v-card>
              </router-link>
            </div>
          </v-sheet>
        </v-carousel-item>
      </v-carousel>
    </div>
    <div class="blog-header" v-if="isType">
      <div class="category">THỂ LOẠI: {{ isType }}</div>
    </div>
    <div class="blog-center">
      <div class="blog-content">
        <h4 class="cat-title">Bài viết mới nhất</h4>
        <div class="lst-blog" v-if="isLoading === true">
          <v-skeleton-loader
            type="card, list-item-two-line"
            v-for="i in 4"
            :key="i"
          ></v-skeleton-loader>
        </div>
        <div class="lst-blog" v-else-if="isLoading === false">
          <router-link
            class="blog-content-item"
            style="text-decoration: none"
            :to="`/blogspage/${blog.id}`"
            v-for="blog in listBlog"
            :key="blog.id"
          >
            <div class="blog-img">
              <img
                v-if="extractImageUrl(blog.content)"
                class="img"
                :src="extractImageUrl(blog.content)"
                :alt="blog.user.avatar"
              />
              <img
                v-if="extractImageUrl(blog.content) == null"
                class="img"
                src="@\assets\photos\blog1.jpg"
                :alt="blog.user.avatar"
              />
            </div>
            <div class="blog-write">
              <div class="blog-cateogory">
                <label>{{ getBlogType(blog) }} - </label>
                <label> {{ " " + getBlogCreatedDate(blog) }}</label>
              </div>
              <div class="blog-title">
                <h3>{{ blog.title }}</h3>
              </div>
              <div class="blog-desc">
                <label>{{ extractPlainText(blog.content) }}</label>
              </div>
              <div class="blog-writer">
                <div class="blog-avatar">
                  <img
                    v-if="blog.user.avatar"
                    class="avatar"
                    :src="blog.user.avatar"
                  />
                  <img
                    v-if="!blog.user.avatar"
                    class="avatar"
                    src="@\assets\photos\avatar.png"
                  />
                </div>
                <div class="writer">
                  <label>{{ getBlogAuthor(blog) }}</label>
                </div>
              </div>
            </div>
          </router-link>
        </div>
        <div class="paging" v-if="!showLoading">
          <v-pagination
            :length="TotalCount"
            v-model="pagein"
            prev-icon="ti ti-chevron-left"
            next-icon="ti ti-chevron-right"
            :total-visible="7"
          ></v-pagination>
        </div>
      </div>
      <div class="blog-sidebar">
        <div class="sidebar-header">
          <v-sheet class="mx-auto sidebar-header-list" max-width="600">
            <v-slide-group
              show-arrows
              prev-icon="ti ti-chevron-left"
              next-icon="ti ti-chevron-right"
            >
              <v-slide-group-item
                v-for="n in listBlogType"
                :key="n"
                v-slot="{ isSelected }"
              >
                <v-btn
                  class="ma-2"
                  rounded
                  :color="isSelected ? 'primary' : undefined"
                  @click="selectType(n.id)"
                >
                  {{ n.name }}
                </v-btn>
              </v-slide-group-item>
            </v-slide-group>
          </v-sheet>
        </div>
        <div class="sidebar-content">
          <router-link
            class="sider-items"
            v-for="index in listBlogSidebar"
            :key="index"
            style="text-decoration: none"
            :to="`/blogspage/${index.id}`"
          >
            <div class="siderbar-img">
              <img
                v-if="extractImageUrl(index.content)"
                class="img"
                :src="extractImageUrl(index.content)"
              />
              <img
                v-if="!extractImageUrl(index.content)"
                class="img"
                src="@\assets\photos\blog1.jpg"
              />
            </div>
            <div class="blog-content-sidebar">
              <div class="blog-cateogory">
                <label>{{ getBlogAuthor(index) }}</label>
                <label>{{ getBlogCreatedDate(index) }}</label>
              </div>
              <div class="blog-title">
                <h5>{{ index.title }}</h5>
              </div>
            </div>
          </router-link>
        </div>
        <div class="siderbar-tags">
          <h3 class="cat-title">Thể loại</h3>
          <div
            class="siderbar-tags-list"
            style="display: flex; flex-wrap: wrap"
          >
            <v-chip-group
              v-for="index in listBlogType"
              :key="index"
              @click="loadBlog(index.id, index.name)"
            >
              <v-chip filter color="green">{{ index.name }}</v-chip>
            </v-chip-group>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import { GET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
export default {
  name: "BlogsPage",
  components: {},
  props: ["typeId", "typeName"],
  created() {
    if (this.typeId && this.typeName) {
      this.loadBlog(this.typeId, this.typeName);
    } else {
      this.loadBlog();
    }
    this.loadBlogType();
    this.loadBlogForType();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    selectType(id) {
      this.BlogTypeId = id;
      this.loadBlogForType();
    },

    async loadBlog(id, name) {
      this.isLoading = true;
      try {
        let type = id; //lưu dữ liệu tìm kiếm
        if (type == null) {
          //nếu không có, mặc định là rỗng
          type = "";
        } else {
          this.isType = name;
        }
        const filter = await `?branchId=${
          this.getBranch.id
        }&typeId=${type}&size=5&page=${this.pagein - 1}`;
        await fetch(`${apiConfig}/blogs${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.listBlog = data.content;
            this.TotalCount = data.totalPages;
            this.listBlog.map(async (blog) => {
              blog.user.avatar = await fireStorage.displayImage(
                blog.user.avatar
              );
            });
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      finally{
        this.isLoading = false;
      }
    },

    async loadBlogForType() {
      this.showLoading = true;
      try {
        var type = this.BlogTypeId; //lưu dữ liệu tìm kiếm
        if (type == null) {
          //nếu không có, mặc định là rỗng
          type = "";
        }
        const filter = `?branchId=${this.getBranch.id}&typeId=${type}&size=5`;
        fetch(`${apiConfig}/blogs${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.listBlogSidebar = data.content;
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.showLoading = false;
    },

    async loadBlogType() {
      this.showLoading = true;
      try {
        fetch(`${apiConfig}/blog-types`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.listBlogType = data;
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.showLoading = false;
    },
    getBlogType(blog) {
      let blogType = blog.type;
      return blogType.name;
    },
    getBlogAuthor(blog) {
      let blogAuthor = blog.user;
      return blogAuthor.fullName;
    },
    getBlogCreatedDate(blog) {
      let blogDate = this.formatUnixTime(blog.createdDate);
      return blogDate;
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
    /**
    Hàm hiện thị thông báo
     */
    ShowToast(Tstatus, message) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Thành công",
        };

        //trường hợp toast cập nhật thành công
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Sửa thành công",
        };
        //trường hợp toast hành động thất bại
      } else {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: message,
        };
      }
      this.displayToast(this.ToastInfor);
    },
  },
  watch: {
    async pagein() {
      await this.loadBlog();
    },
    async getBranch() {
      await this.loadBlog();
      await this.loadBlogForType();
    },
  },
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_HOME_SELECTED,
    }),
  },
  data() {
    return {
      isLoading: false,
      showLoading: false,
      listBlog: [],
      listBlogType: [],
      colors: [
        "white",
        "warning",
        "pink darken-2",
        "red lighten-1",
        "deep-purple accent-4",
      ],
      slides: ["First", "Second", "Third", "Fourth", "Fifth"],
      listAuthor: {},
      BlogTypeId: null,
      listBlogSidebar: [],
      isType: null,
      TotalCount: 1,
      pagein: 1,
    };
  },
};
</script>

<style scoped>
.blog-container {
  display: block;
  min-height: 100vh;
  padding: 2% 5%;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}

.lst-blog {
  min-height: 100vh;
}
.sheet {
  position: relative;
}
.sider-img {
  width: 100%;
  height: 100%;
  position: relative;
}
.w3-display-container {
  margin-bottom: 50px !important;
}
.card {
  width: 100%;
  max-height: 300px;
  position: absolute;
  top: 67%;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5) !important;
  color: #fff;
}
.h-100 {
  height: 120% !important;
}
.h-100 {
  height: 120% !important;
}
.h-100{
  height: 120% !important;
}
.blog-center {
  width: 100%;
  min-height: 100vh;
  display: flex;
}
.blog-content {
  width: 70%;
  display: block;
  float: left;
  opacity: 0;
  animation: fadeIn 1.5s ease-out forwards;
  padding: 20px;
  border-radius: 5px;
  background-color: #fff;
}
.blog-content-sidebar {
  padding-left: 20px;
  width: 70%;
  display: block;
  float: left;
  opacity: 0;
  animation: fadeIn 1.5s ease-out forwards;
  border-radius: 5px;
  background-color: #fff;
}
.blog-sidebar {
  width: 30%;
  margin-left: 7%;
  float: right;
  opacity: 0;
  transform: translateX(-20px);
  animation: fadeIn 1.5s ease-out forwards;
  padding: 20px;
  border-radius: 5px;
  background-color: #fff;
}
.blog-content-item {
  width: 100%;
  height: 300px;
  display: flex;
  margin-bottom: 50px;
  cursor: pointer;
}
.blog-img {
  width: 50%;
  height: 100%;
}

.img {
  width: 100%;
  height: 100%;
}
.blog-write {
  width: 45%;
  height: 100%;
  margin-left: 40px;
}
.blog-cateogory {
  display: flex;
  font-size: 11px;
  letter-spacing: 0.07rem;
  text-transform: uppercase;
  font-weight: bold;
  font-family: var(--font-secondary);
  color: #0000009e;
  margin-bottom: 10px;
}
.blog-cateogory label {
  padding-left: 5px;
}
.blog-title {
  display: flex;
  text-align: left;
  padding: 0 0 20px 0;
}
.blog-desc {
  display: flex;
  text-align: left;
  min-height: 100px;
  max-height: 105px;
  overflow: hidden;
  cursor: pointer;
  padding: 0 0 20px 0;
}
.blog-desc label {
  cursor: pointer;
}
.blog-writer {
  width: 100%;
  height: 75px;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.blog-writer.b {
  height: 30px;
}
.blog-avatar {
  width: 45px;
  height: 45px;
  border-radius: 30px;
}
.avatar {
  width: 100%;
  height: 100%;
  border-radius: 30px;
}
.writer {
  width: 60%;
  overflow: hidden;
  font-size: 16px;
  display: flex;
  padding: 0 20px;
}
.writer label {
  cursor: pointer;
}
.sidebar-header {
  font-size: 20px;
  text-transform: uppercase;
  line-height: 30px;
  position: relative;
  padding: 0 0 25px 0;
  font-weight: bold;
}
.sidebar-content {
  width: 100%;
  max-height: 70%;
}
.sider-items {
  overflow: hidden;
  clear: both;
  padding: 30px 0;
  cursor: pointer;
  position: relative;
  border-bottom: 1px solid rgb(216, 211, 211);
  display: flex;
}
.sidebar-header-list {
  background-color: #fff;
}
.banner-des {
  height: 100px;
  font-size: 16px;
  margin-bottom: 40px;
  text-overflow: ellipsis;
}
.banner-title {
  width: 100%;
  height: 60px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.title h3 {
  font-size: 30px;
}
.siderbar-tags {
  width: 100%;
  height: 20%;
  margin-top: 50px;
}
.tags-header {
  display: flex;
  text-transform: uppercase;
  letter-spacing: 0.1rem;
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 30px;
  border-bottom: 2px solid var(--color-black);
}
.siderbar-tags-list {
  display: flex;
  padding-left: 0;
  list-style: none;
  margin-top: 0;
  margin-bottom: 1rem;
}
.blog-header {
  display: flex;
  align-items: center;
  height: 80px;
  width: 100%;
}
.sheet {
  height: 100%;
}
.category {
  text-transform: uppercase;
  letter-spacing: 0.1rem;
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 30px;
}
.cat-title {
  font-size: 14px;
  font-weight: 600;
  background: #04091e;
  color: #fff;
  padding: 10px 25px;
  display: flex;
  margin-bottom: 30px;
}
.siderbar-img {
  width: 40%;
  height: 50%;
}
.paging {
  width: 100%;
  height: 5%;
}
@media (max-width: 1200px) {
  .w3-display-container {
    display: none;
  }
  .blog-container {
    width: 100%;
    padding: 2% 5%;
  }
  .blog-center {
    display: block;
  }
  .blog-content {
    width: 100%;
    padding: 15px;
    margin-bottom: 30px;
  }
  .blog-content-item {
    display: block;
    height: 100%;
  }
  .blog-img {
    width: 100%;
  }
  .blog-write {
    width: 100%;
    margin: 0;
    margin-top: 20px;
  }
  .blog-title {
    text-align: -webkit-auto;
  }
  .blog-sidebar {
    width: 100%;
    display: block;
    margin-left: 5%;
    float: left;
  }
}
</style>