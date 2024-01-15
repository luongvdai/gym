<template>
  <div class="blogdetail-container">
    <div class="blog-center">
      <div class="blog-content" v-if="showLoading">
        <v-skeleton-loader
          min-width="320"
          type="image, article"
        ></v-skeleton-loader>
      </div>
      <div class="blog-content" v-else>
        <div class="blog-write">
          <div class="blog-cateogory b">
            <label>{{ this.blogTypeName }} - </label>
            <label>{{ blogCreatedDate }}</label>
          </div>
          <div class="blog-writer">
            <div class="blog-avatar">
              <img
                class="avatar"
                v-if="!this.authorImage"
                src="@\assets\photos\avatar.png"
              />

              <img class="avatar" v-if="this.authorImage" :src="authorImage" />
            </div>
            <div class="writer">
              <label>{{ this.author.fullName }}</label>
            </div>
          </div>
          <div class="blog-title">
            <h1>{{ this.BlogObject.title }}</h1>
          </div>
          <div class="blog-desc" v-html="this.BlogObject.content"></div>
        </div>
      </div>
      <div class="blog-sidebar">
        <div class="sidebar-header">
          <label>BÀI VIẾT KHÁC</label>
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
          <h3 class="tags-header">Thể loại</h3>
          <div
            class="siderbar-tags-list"
            style="display: flex; flex-wrap: wrap"
          >
            <v-chip-group v-for="index in listType" :key="index">
              <v-chip color="green">
                <router-link
                  class="tags-link"
                  :to="`/blogspage?typeId=${index.id}&typeName=${index.name}`"
                >
                  {{ index.name }}
                </router-link>
              </v-chip></v-chip-group
            >
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
import { mapMutations, mapGetters } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
import { GET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
export default {
  mounted() {
    this.blogId = this.$route.params.id;
    this.getBlogById(this.blogId);
    this.loadBlogType();
  },
  beforeRouteUpdate(to, from, next) {
    this.blogId = to.params.id;
    this.getBlogById(this.blogId);
    this.loadBlogType();
    next();
  },
  created() {},
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    async getBlogById(blogId) {
      try {
        fetch(`${apiConfig}/blogs/${blogId}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.id) {
              this.BlogObject = data;
              this.blogTypeName = this.getBlogType(this.BlogObject);
              this.loadBlogTypeById(data.type.id);
              this.author = this.BlogObject.user;
              (async () => {
                this.authorImage = await fireStorage.displayImage(
                  this.author.avatar
                );
              })();

              this.blogCreatedDate = this.getBlogCreatedDate(this.BlogObject);
              this.BlogObject.content = `<style scoped>img{width:100% !important; height: 100% !important; p{width:100%;}</style>${this.BlogObject.content}`;
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async loadBlogTypeById(id) {
      this.showLoading = true;
      try {
        var type = id; //lưu dữ liệu tìm kiếm
        if (type == null) {
          //nếu không có, mặc định là rỗng
          type = "";
        }
        const filter = `?branchId=${this.getBranch.id}&typeId=${type}&size=5`;
        fetch(`${apiConfig}/blogs${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.listBlogSidebar = data.content;
            }
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
          .then(async (data) => {
            this.listType = data;
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
    async getBranch() {
      await this.loadBlogTypeById();
    },
  },
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_HOME_SELECTED,
    }),
  },
  data() {
    return {
      blogId: null,
      BlogObject: {},
      blogTypeName: null,
      blogCreatedDate: null,
      authorImage: null,
      author: {},
      listBlogSidebar: [],
      listType: [],
      showLoading: false,
    };
  },
};
</script>

<style scoped>
.blogdetail-container {
  display: block;
  min-height: 100vh;
  padding: 2% 10%;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}
.blog-center {
  width: 100%;
  min-height: 100vh;
  display: flex;
  margin-top: 50px;
}
.blog-content {
  width: 65%;
  display: block;
  float: left;
  padding: 30px;
  background-color: #fff;
  border-radius: 10px;
}
.blog-sidebar {
  width: 35%;
  margin-left: 8%;
  float: right;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
}
.blog-content-item {
  width: 100%;
  height: 300px;
  display: flex;
  margin-bottom: 50px;
  cursor: pointer;
}
.blog-img {
  width: 100%;
  height: 50%;
}
.img {
  width: 100%;
  height: 100%;
}
.blog-write {
  width: 100%;
  height: 100%;
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
.blog-cateogory.b {
  background: #e9485a;
  color: #fff;
  padding: 4px 30px;
  font-weight: 300;
  width: fit-content;
}
.blog-cateogory label {
  padding-left: 5px;
}
.blog-title {
  display: flex;
  text-align: left;
  height: fit-content;
}
.blog-title.b {
  height: 60px;
}
.blog-desc {
  margin-top: 10%;
  text-align: left;
  min-height: 150px;
  width: 100%;
}
.image img {
  width: 100%;
  height: 100%;
}
.blog-writer {
  width: 100%;
  height: 75px;
  display: flex;
  align-items: center;
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
  width: 25%;
  overflow: hidden;

  font-size: 16px;
}
.sidebar-header {
  font-size: 17px;
  text-transform: uppercase;
  line-height: 30px;
  margin: 0 0 30px 0;
  position: relative;
  font-weight: bold;
  background: #04091e;
  color: #fff;
  padding: 5px 20px;
}
.sidebar-content {
  width: 100%;
  max-height: 72%;
}
.sider-items {
  overflow: hidden;
  clear: both;
  margin-bottom: 30px;
  cursor: pointer;
  position: relative;
  border-bottom: 1px solid rgb(216, 211, 211);
  display: flex;
}
.sidebar-header-list {
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
.siderbar-img {
  width: 40%;
  height: 50%;
}
.banner-des {
  height: 150px;
  overflow: hidden;
  margin-bottom: 40px;
}
.banner-title {
  width: 500px;
}
.blog-desc {
  width: 100% !important;
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
  font-weight: bold;
  background: #04091e;
  color: #fff;
  padding: 5px 20px;
}
.siderbar-tags-list {
  display: flex;
  padding-left: 0;
  list-style: none;
  margin-top: 0;
  margin-bottom: 1rem;
}
.tags-link {
  text-decoration: none;
}
@media (max-width: 1200px) {
  .blogdetail-container {
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
  .blog-title h1 {
    font-size: 20px;
  }

  .blog-sidebar {
    width: 100%;
    display: block;
    margin-left: 5%;
    float: inline-end;
  }
  .blog-desc figure {
    margin: 1em 0 !important;
  }
  .blog-desc {
    margin: 0;
  }
}
</style>
