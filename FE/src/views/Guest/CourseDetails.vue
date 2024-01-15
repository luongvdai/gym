<template>
  <div class="course-detail-container">
    <div class="banner">
      <v-carousel :show-arrows="false" hide-delimiters>
        <v-carousel-item
          src="https://cali.vn/storage/app/media/2022/Challenges/California%20Transformation/CTC-headbanner-1900x800.jpg"
          cover
        ></v-carousel-item>
      </v-carousel>
      <div class="banner-content">
        <div class="banner-content-label">
          <label class="banner-label">THỬ THÁCH</label>
          <label class="banner-label">THAY ĐỔI HÌNH THỂ</label>
        </div>
        <div class="banner-content-btn">
          <a href="#Sign">
            <MButton
              :text="'Tìm hiểu thêm'"
              class="button-addtocard"
              :class="'content-top-btn-content'"
            />
          </a>
        </div>
      </div>
    </div>
    <div class="content-top">
      <div class="top-left">
        <div class="top-left-label">
          <label class="header-label">TINH THẦN RỰC LỬA VỚI </label>
          <label class="header-label">{{ this.courseSelected.title }}</label>
        </div>
        <div class="top-left-content">
          <label class="content-label"
            >{{ this.courseSelected.description }}
          </label>
        </div>
        <div class="banner-content-btn">
          <a href="#Sign">
            <MButton
              :text="'Đăng ký khóa học ngay'"
              class="button-addtocard"
              :class="'content-top-btn-content'"
            />
          </a>
        </div>
      </div>
      <div class="banner-top-right">
        <v-carousel hide-delimiters show-arrows="hover">
          <v-carousel-item
            v-for="(item, i) in items"
            :key="i"
            :src="item.src"
            cover
          ></v-carousel-item>
        </v-carousel>
      </div>
    </div>
    <div class="content-center">
      <div class="content-center-container">
        <div class="content-center-header">
          <label class="tip">Quy trình tham gia</label>
        </div>
        <div class="process">
          <div class="item">
            <div class="step">
              <div class="step-title">Bước</div>
              <div class="step-number">01</div>
            </div>
            <div class="step-content">
              Đăng ký tài khoản hội viên và ghi nhận chỉ số hình thể
            </div>
          </div>
          <div class="item">
            <div class="step">
              <div class="step-title">Bước</div>
              <div class="step-number">02</div>
            </div>
            <div class="step-content">
              Lựa chọn khóa tập, Huấn Luyện Viên phù hợp với bạn
            </div>
          </div>
          <div class="item">
            <div class="step">
              <div class="step-title">Bước</div>
              <div class="step-number">03</div>
            </div>
            <div class="step-content">
              Thanh toán và được chuyên gia tư vấn về lịch tập, chế độ ăn uống
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="content-heath">
      <div class="content-heath-container">
        <div class="content-heath-header">
          <label class="tip"
            >{{ this.courseSelected.title }} <em>có tốt không?</em></label
          >
        </div>
        <div class="heath">
          <div class="heath-left">
            <div class="heath-item">
              <div class="heath-item-icon">
                <i class="ti ti-recharging"></i>
              </div>
              <div class="heath-item-content">
                <div class="heath-item-label">
                  <label>Nhanh</label>
                </div>
                <div class="heath-item-description">
                  <label
                    >Mỗi buổi tập chỉ kéo dài 20 phút (5 phút thay đồ). Tham gia
                    1 hoặc 2 bài tập mỗi tuần để nhận được kết quả tốt
                    nhất.</label
                  >
                </div>
              </div>
            </div>
            <div class="heath-item">
              <div class="heath-item-icon">
                <i class="ti ti-trophy"></i>
              </div>
              <div class="heath-item-content">
                <div class="heath-item-label">
                  <label>Thành công</label>
                </div>
                <div class="heath-item-description">
                  <label
                    >Chỉ sau vài buổi tập luyện, bạn có thể nhận thấy những cải
                    thiện về thể chất, chẳng hạn như bớt đau lưng hoặc da căng
                    và săn chắc hơn.</label
                  >
                </div>
              </div>
            </div>
            <div class="heath-item">
              <div class="heath-item-icon">
                <i class="ti ti-clock-hour-3"></i>
              </div>
              <div class="heath-item-content">
                <div class="heath-item-label">
                  <label>Tiết kiệm thời gian</label>
                </div>
                <div class="heath-item-description">
                  <label
                    >Cường độ tập luyện tác động đến cơ thể cao hơn nhiều lần so
                    với tập luyện bình thường.</label
                  >
                </div>
              </div>
            </div>
          </div>
          <div class="heath-right">
            <div class="heath-item">
              <div class="heath-item-icon">
                <i class="ti ti-rocket"></i>
              </div>
              <div class="heath-item-content">
                <div class="heath-item-label">
                  <label>Hiệu quả</label>
                </div>
                <div class="heath-item-description">
                  <label
                    >Tập luyện với toàn bộ cơ thể. Xung điện tác động sâu đến
                    các bó cơ trong cơ thể</label
                  >
                </div>
              </div>
            </div>
            <div class="heath-item">
              <div class="heath-item-icon">
                <i class="ti ti-barbell"></i>
              </div>
              <div class="heath-item-content">
                <div class="heath-item-label">
                  <label>Dễ thực hiện</label>
                </div>
                <div class="heath-item-description">
                  <label
                    >Những bài tập mà bạn thực hiện trên thiết bị rất đơn giản
                    và dễ nhớ. Người tập sẽ được các huấn luyện viên cá nhân dẫn
                    dắt, hướng dẫn các bài tập.</label
                  >
                </div>
              </div>
            </div>
            <div class="heath-item">
              <div class="heath-item-icon">
                <i class="ti ti-brand-google-fit"></i>
              </div>
              <div class="heath-item-content">
                <div class="heath-item-label">
                  <label>Sức khỏe tốt</label>
                </div>
                <div class="heath-item-description">
                  <label
                    >Tập luyện có thể có tác động tích cực đến nhiều vấn đề sức
                    khỏe (vấn đề về đĩa đệm, không tự chủ, v.v.)</label
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="sign-course" id="Sign">
      <div class="sign-course-container">
        <div class="sign-course-content">
          <div class="sign-course-left left" :class="{ show: isVisibleLeft }">
            <div class="sign-course-title">
              <label>TRẢI NGHIỆM KHÓA TẬP NGAY HÔM NAY</label>
            </div>
            <div class="sign-course-readmore">
              <label
                >Khi phát sinh thanh toán, vui lòng chỉ thanh toán cho công ty
                Fitness & Yoga, không giao dịch hay chuyển khoản vào tài khoản
                không phải của Công ty.</label
              >
            </div>
          </div>
          <div
            class="sign-course-right right"
            :class="{ show: isVisibleRight }"
          >
            <div class="top-right label">
              <label class="course-name-label"
                >{{ this.courseSelected.title }}
              </label>
            </div>
            <div class="top-right time">
              <label class="course-time-label"
                >Thời lượng {{ this.courseSelected.rangeProcess }} tháng</label
              >
            </div>
            <div class="top-right label">
              <div>
                <span class="font-weight-bold">{{
                  FormatVND(this.courseSelected.price)
                }}</span>
              </div>
            </div>
            <div class="top-right dropdown">
              <div class="input-top">
                <label class="input-label">Chọn huấn luyện viên</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 100%;'"
                  :inValueCombox="inValue_Unit"
                  :DropboxItem="PTList"
                  :value="'id'"
                  :label="'username'"
                  :code="'gender'"
                  :CombolabelLeft="'Tên'"
                  :CombolabelRight="'Giới tính'"
                  :isShow="isShowDropbox"
                  :maxlength="100"
                  :isInfor="true"
                  @combobox-value="getPT"
                  @handlSearchCombobox="searchCombobox"
                  :onSearch="isSearching"
                  ref="combobox"
                  :tab="1"
                />
              </div>
            </div>
            <div class="top-right btn" @click="openAsk">
              <MButton
                :text="'Đăng ký khóa học'"
                class="button-addtocard"
                :class="'content-top-btn-content'"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="content-bottom">
      <div class="bottom-course-header">
        <label class="similar-header">Khóa học tương tự</label>
      </div>
      <v-slide-group v-model="model" selected-class="bg-success" show-arrows>
        <v-slide-group-item v-for="course in lstCourses" :key="course.id">
          <card-course :class="['ma-4', selectedClass]" :course="course" />
        </v-slide-group-item>
      </v-slide-group>
    </div>
    <div class="mpopup-ask">
      <MPopupNotification
        v-if="isShowAskLogin"
        :errors="byError"
        :btnOut="btnOut"
        :classlabel="'popupask-icon-label-ask'"
        :classcssicon="'popupask-icon icon-ask'"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="goToLogin"
        @close-notification-click="ClosePopupAsk"
        :MPopupN="1"
      />
    </div>
  </div>
</template>

<script>
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MButton from "@/components/MButton/MButton.vue";
import CardCourse from "@/components/MCard/CardCourse.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { GET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  components: {
    MCombobox,
    MButton,
    CardCourse,
    MPopupNotification,
  },
  props: {},

  created() {
    (async () => {
      await this.getCourseById(this.$route.params.id);
    })();
    this.getPtByCourseId(this.$route.params.id);
    this.loadAllCourses();
  },
  beforeMount() {
    this.getLocal();
  },
  beforeRouteUpdate(to, from, next) {
    if (to.params.id !== from.params.id) {
      (async () => {
        await this.getCourseById(to.params.id);
        await this.getPtByCourseId(to.params.id);
        await this.loadAllCourses();
      })();
    }

    next();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },

  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    handleScroll() {
      try {
        const leftElement = this.$el.querySelector(".left");
        const rightElement = this.$el.querySelector(".right");

        if (leftElement && this.isInViewport(leftElement)) {
          this.isVisibleLeft = true;
          window.removeEventListener("scroll", this.handleScroll);
        }

        if (rightElement && this.isInViewport(rightElement)) {
          this.isVisibleRight = true;
          window.removeEventListener("scroll", this.handleScroll);
        }
      } catch (error) {
        console.log(error);
      }
    },
    isInViewport(element) {
      try {
        var rect = element.getBoundingClientRect();
        return (
          rect.top >= 0 &&
          rect.bottom <=
            (window.innerHeight || document.documentElement.clientHeight)
        );
      } catch (error) {
        console.log(error);
      }
    },

    getLocal() {
      try {
        this.branchId = JSON.parse(localStorage.getItem("userData"))[
          "branchId"
        ];
        this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
      } catch (error) {
        console.log();
      }
    },

    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskLogin = false; //đóng popup hỏi người dùng
    },

    openAsk() {
      if (!this.branchId) {
        this.isShowAskLogin = true;
        this.byError = "Bạn cần đăng nhập để đăng ký";
        return;
      }

      if (this.branchId == this.getBranch.id) {
        this.getPtCourseId();
      } else {
        this.isShowAskLogin = true;
        this.btnOut = false;
        this.byError = "Không được mua khóa tập khác chi nhánh!";
      }
    },

    goToLogin() {
      this.$router.push("/login");
    },

    getPT(value) {
      this.ptId = value;
    },

    async getCourseById(id) {
      try {
        await fetch(`${apiConfig}/courses/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.id) {
              this.courseSelected = data;
            } else {
              this.$router.push(`/courses/${id}/notfound`);
              this.ShowToast(3, "Không tìm thấy khóa tập");
            }
          })
          .catch(() => {
            this.$router.push(`/courses/${id}/notfound`);
            this.ShowToast(3, "Không tìm thấy khóa tập");
          });
      } catch (error) {
        this.$router.push(`/courses/${id}/notfound`);
        console.log(error);
        this.ShowToast(3, "Không tìm thấy khóa tập");
      }
    },
    async getPtByCourseId(id) {
      try {
        fetch(
          `${apiConfig}/pt-courses-user/mentor?courseId=${id}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.PTList = data.content;
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

    async getPtCourseId() {
      try {
        fetch(
          `${apiConfig}/pt-courses/info?courseId=${this.$route.params.id}&ptId=${this.ptId}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.PTCourse = data;
            this.SignCourse();
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

    async SignCourse() {
      this.isShowAskLogin = false; //đóng popup hỏi người dùng
      try {
        fetch(
          `${apiConfig}/prepare-orders`,
          await fetchOptions("POST", {
            title: null,
            ptCourseId: this.PTCourse.id,
          })
        )
          .then((res) => res.json().catch(() => res))
          .then((res) => {
            if (!res.id) {
              this.ShowToast(3, res.message);
            } else {
              this.ShowToast(1);
              setTimeout(() => {
                this.$router.push("/cartpage");
              }, 500);
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Đăng ký thất bại");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async loadAllCourses() {
      try {
        this.showLoading = true;
      let filter = `?branchId=${this.getBranch.id}&size=5`;
      fetch(`${apiConfig}/courses${filter}`, await fetchOptions("GET"))
        .then((response) => response.json().catch(() => response))
        .then((response) => {
          this.lstCourses = response.content;
          this.showLoading = false;
        })
        .catch((error) => {
          console.log(error.message);
            this.ShowToast(3, "Lỗi máy chủ");
          this.showLoading = false;
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.showLoading = false;
    },

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
          message: "Thêm thành công",
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
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_HOME_SELECTED,
    }),
  },
  watch: {
    getBranch() {
      this.loadAllCourses();
      if (
        this.roleValue != null &&
        this.branchId &&
        this.branchId != "NOT_YET" &&
        this.branchId != this.getBranch.id
      ) {
        this.$router.push("/courses");
      }
    },
  },
  data() {
    return {
      courseSelected: {},
      drop: true,
      PTList: {},
      isShowAskLogin: false,
      branchId: null,
      byError: null,
      btnOut: true,
      lstCourses: [],
      showLoading: false,
      PTCourse: {},
      roleValue: null,
      isVisibleLeft: false,
      isVisibleRight: false,
      items: [
        { src: "https://citigym.com.vn/storage/uploads/thumbnail-3-2.jpg" },
        {
          src: "https://peacefulqode.com/themes/gymster/html/images/blog-images/blog-1.jpg",
        },
      ],
    };
  },
};
</script>

<style scoped>
.course-detail-container {
  width: 100%;
  display: block;
  min-height: 100vh;
  background-color: #ecf0f1;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}
.banner {
  width: 100%;
  height: 800px;
  background-color: black;
  position: relative;
}
.banner-content {
  width: 500px;
  height: 200px;
  position: absolute;
  top: 50%;
  left: 10%;
}
.banner-content-label {
  width: 100%;
  height: 75%;
  display: grid;
}
.v-window {
  height: 100% !important;
}
.banner-label {
  font-size: 50px;
  font-weight: bold;
  color: #ffff;
}
.banner-content-btn {
  width: 100%;
  height: 25%;
  padding: 10px;
}
.content-top {
  width: 100%;
  height: 612px;
  display: flex;
  padding: 53px;
}
.top-left {
  width: 43%;
  height: 100%;
  padding: 3%;
  background-color: #fff;
  border-radius: 10px;
}
.top-left-label {
  width: 100%;
  height: 22%;
  display: grid;
  line-height: 50px;
}
.header-label {
  font-family: system-ui;
  font-size: 30px;
  font-weight: bold;
  color: #04091e;
  text-transform: uppercase;
}
.top-left-content {
  padding: 6%;
}
.content-label {
  font-size: 18px;
  font-weight: lighter;
  color: #000;
}
.banner-top-right {
  width: 57%;
  height: 100%;
  padding: 4px 2%;
}
.v-img__img--cover {
  border-radius: 10px !important;
}
.banner-top-right-img {
  width: 80%;
  height: 80%;
}
.content-bottom {
  width: 100%;
  height: 600px;
  padding: 0 3%;
}
.bottom-course-header {
  font-weight: bold;
  font-size: 24px;
  margin-bottom: 20px;
  text-align: left;
  width: 100%;
  background: #04091e;
  color: #fff;
  padding: 10px 25px;
  display: flex;
}
.bottom-cousrse-content {
  width: 20%;
  height: 100%;
}
.bottom-course {
  font-weight: bold;
  font-size: 14px;
  text-align: left;
  margin-bottom: 5px;
}

.bottom-course.img {
  height: 75%;
  width: 90%;
}
.similar-course-img {
  height: 100%;
  width: 100%;
}
.content-center {
  width: 100%;
  height: 500px;
  padding: 10px 3%;
  margin-bottom: 30px;
}
.content-center-container {
  width: 100%;
  height: 100%;
  background-color: #fff;
  padding: 20px 2%;
  border-radius: 10px;
}
.content-center-header {
  font-weight: bold;
  font-size: 23px;
  text-align: left;
  width: 100%;
  background: #04091e;
  color: #fff;
  padding: 10px 25px;
  display: flex;
}
.process {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 80%;
}
.item {
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: row;
  width: 100%;
  margin: 20px 0;
  transition: transform 0.3s ease-in-out;
}
.item:hover {
  transform: scale(1.1);
}
.step {
  line-height: 21px;
  color: #04091e;
  height: 210px;
  min-width: 210px;
  max-height: 210px;
  max-width: 210px;
  border-radius: 100%;
  border: 2px solid #04091e;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.step-number {
  font-style: normal;
  font-weight: bold;
  font-size: 64px;
  line-height: 95px;
  text-align: center;
  color: #04091e;
}
.step-title {
  font-size: 20px;
  font-weight: bold;
}
.step-content {
  font-weight: bold;
  font-size: large;
  display: flex;
  justify-content: left;
  align-items: center;
  width: 75%;
  max-width: 250px;
}
.content-heath {
  width: 100%;
  height: 600px;
  padding: 0 3%;
}
.content-heath-container {
  width: 100%;
  height: 100%;
  padding: 2% 2%;
  background-color: #fff;
  border-radius: 10px;
}
.content-heath-header {
  font-weight: bold;
  font-size: 30px;
  margin-bottom: 20px;
  text-align: center;
  width: 100%;
}
.heath {
  width: 70%;
  height: 80%;
  margin: 0 15%;
  display: flex;
}

.heath-left {
  width: 50%;
  height: 100%;
}
.heath-right {
  width: 50%;
  height: 100%;
}
.heath-item {
  padding: 20px 20px 18px;
  display: flex;
  align-items: center;
  text-align: left;
  transition: transform 0.3s ease-in-out;
}
.heath-item:hover {
  transform: scale(1.1);
  border: 1px solid #bbbb;
  border-radius: 15px;
  background-color: #fff;
}
.heath-item-icon {
  margin-right: 15px;
  width: 80px;
  height: 80px;
  font-size: 60px;
  color: #2ca01c;
}
.heath-item-content {
  width: 80%;
  height: 100%;
}
.heath-item-label {
  font-size: 20px;
  font-weight: 600;
}
.heath-item-description {
  font-weight: 400;
  font-size: 16px;
}
.sign-course {
  width: 100%;
  height: 600px;
  padding: 2% 3%;
}
.sign-course-container {
  width: 100%;
  height: 100%;
  padding: 2% 12%;
  background-color: #fff;
  border-radius: 10px;
}
.sign-course-content {
  width: 100%;
  height: 100%;
  display: flex;
}
.sign-course-left {
  width: 50%;
  height: 100%;
  position: relative;
  opacity: 0;
  transform: translateX(-50%);
  transition: opacity 0.5s ease-in-out, transform 0.5s ease-in-out;
}
.show {
  opacity: 1;
  transform: translateX(0);
}
.sign-course-title {
  font-family: system-ui;
  color: #04091e;
  width: 90%;
  height: 45%;
  font-size: 50px;
  font-weight: bold;
  display: flex;
  align-items: center;
}
.sign-course-readmore {
  width: 100%;
  font-size: 15px;
  text-align: start;
  padding: 0px 70px;
  align-items: center;
  margin-top: 14px;
}
.sign-course-right {
  width: 50%;
  height: 100%;
  position: relative;
  padding: 3% 0;
}

.top-right {
  height: 8%;
  width: 100%;
  display: flex;
  align-items: center;
}
.top-right.time {
  color: #828282;
}
.top-right.label {
  font-size: 18px;
  height: 10%;
}
.top-right.dropdown {
  margin-bottom: 30px;
  width: 62%;
  height: 13%;
  display: block;
  position: relative;
}
.input-bottom {
  width: 90%;
  height: 72%;
}
.top-right.btn {
  margin-bottom: 30px;
  width: 80%;
}
.input-choosept {
  width: 70%;
}
.button-addtocard {
  width: 70%;
}
.course-name-label {
  font-size: 28px;
  font-weight: bold;
}
.course-price {
  font-size: 20px;
  font-weight: bold;
  margin-right: 20px;
}
.course-price.unsale {
  color: #828282;
  text-decoration: line-through;
}
.top-right.text {
  display: block;
  text-align: left;
  width: 70%;
}
.text-header {
  font-size: 15px;
  margin-bottom: 10px;
}
.list-text-main {
  list-style-type: disc;
  padding-left: 20px;
  font-size: 13px;
  line-height: 1.5;
}
.similar-price {
  margin-right: 7px;
}
.input-top {
  width: 80%;
  display: flex;
  align-content: flex-start;
}
.input-label {
  font-size: 15px;
  color: #2ca01c;
  font-weight: bold;
}
.input-label.sao {
  color: red;
}
.v-slide-group {
  width: 98% !important;
}
@media (max-width: 1200px) {
  .banner {
    height: 100%;
  }
  .banner-image {
    width: 100%;
  }
  .banner-content {
    height: 50%;
    width: 40%;
    position: absolute;
    top: 54%;
    left: 0px;
  }
  .banner-label {
    font-size: 16px;
  }
  .banner-content-btn {
    display: none;
  }
  .content-top {
    width: 100%;
    height: 100%;
    display: block;
    padding: 40px 0;
  }
  .content-center {
    height: 100%;
  }

  .top-left {
    width: 100%;
  }
  .banner-top-right {
    width: 100%;
  }
  .process {
    display: block;
  }
  .item {
    padding: 15px 90px;
    margin: 0;
    display: block;
  }
  .step-content {
    width: 113%;
  }
  .step {
    margin-bottom: 10px;
  }
  .content-heath {
    height: 100%;
  }
  .content-heath-header {
    font-size: 27px;
  }
  .heath-item-description {
    display: none;
  }
  .heath {
    margin: 0;
    width: 100%;
  }
  .heath-item {
    padding: 20px 0px 18px;
  }
  .heath-item-label {
    width: 100%;
    font-size: 18px;
  }
  .sign-course {
    height: 100%;
    padding: 0;
  }
  .sign-course-content {
    display: block;
  }
  .sign-course-left {
    width: 100%;
  }
  .sign-course-title {
    width: 100%;
    font-size: 32px;
  }
  .sign-course-right {
    width: 100%;
  }
  .top-right {
    text-align: center;
    display: block;
    height: 40px;
  }
  .top-right.dropdown {
    margin-bottom: 30px;
    width: 100%;
    height: 15%;
    display: block;
    padding: 0px 50px;
  }
  .input-bottom {
    width: 100%;
    height: 35px;
  }
  .top-right.btn {
    width: 100%;
  }.sign-course-container{
    padding: 10% 5%;
  }.sign-course-readmore{
    padding: 0;
  }
}
</style>
