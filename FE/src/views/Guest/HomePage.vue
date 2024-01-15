<template>
  <div id="homepage" class="container">
    <!-- Image Header -->
    <div class="w3-display-container w3-animate-opacity">
      <v-carousel
        class="carousel"
        height="100%"
        interval="3000"
        cycle
        hide-delimiters
        :show-arrows="false"
      >
        <v-carousel-item>
          <v-sheet :color="colors[i]" height="100%">
            <div class="sheet">
              <div class="sider-img bg-orange">
                <img
                  v-if="!urlImage"
                  class="w-100 h-100"
                  src="@\assets\photos\banner1.jpg"
                />
                <img v-if="urlImage" class="w-100 h-100" :src="urlImage" />
                <v-card
                  :elevation="0"
                  class="card"
                  position="absolute"
                  v-bind="props"
                  max-width="450"
                >
                  <v-card-item class="text-start">
                    <v-card-title class="title"
                      ><h1>WORK HARD IN SILENCE</h1>
                      <h3>LET SUCCESS BE YOUR NOICE</h3>
                    </v-card-title>
                  </v-card-item>
                  <v-card-item class="text-justify">
                    <p>
                      {{ Branch.description }}
                    </p>
                  </v-card-item>
                  <v-card-actions>
                    <v-btn
                      variant="elevated"
                      color="#2ca01c"
                      class="text-none"
                      rounded="xl"
                      >Tìm hiểu thêm</v-btn
                    >
                    <v-btn
                      variant="outlined"
                      color="#fff"
                      class="text-none"
                      rounded="xl"
                      >Đăng ký</v-btn
                    >
                  </v-card-actions>
                </v-card>
              </div>
            </div>
          </v-sheet>
        </v-carousel-item>
      </v-carousel>
    </div>
    <section class="program">
      <div class="program-contrainer">
        <div class="program-row">
          <div class="program-header col-lg-6 offset-lg-3">
            <h2 class="sale-course-title">
              LỰA CHỌN <em> LOẠI KHÓA TẬP CHO RIÊNG BẠN</em>
            </h2>
          </div>
          <div class="program-content-left col-lg-6">
            <div class="program-item" v-for="index in leftList" :key="index">
              <div class="program-icon">
                <i :class="index.iconType"></i>
              </div>
              <div class="program-right-content">
                <h4 class="program-name">{{ index.name }}</h4>
                <p>
                  {{ index.description }}
                </p>
                <router-link
                  :to="'/courses?typeId=' + index.id"
                  class="program-link"
                  >Tìm hiểu thêm</router-link
                >
              </div>
            </div>
          </div>
          <div class="program-content-right col-lg-6">
            <div class="program-item" v-for="index in rightList" :key="index">
              <div class="program-icon">
                <i :class="index.iconType"></i>
              </div>
              <div class="program-right-content">
                <h4 class="program-name">{{ index.name }}</h4>
                <p>
                  {{ index.description }}
                </p>
                <router-link
                  :to="'/courses?typeId=' + index.id"
                  class="program-link"
                  >Tìm hiểu thêm</router-link
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="donthink">
      <div class="donthink-contrainer">
        <div class="donthink-row">
          <router-link
            to="/login"
            class="donthink-content"
            v-if="!isAuthenticated"
          >
            <h2 class="donthink-header">
              ĐỪNG <em>CHẦN CHỪ</em>, HÃY ĐĂNG KÝ <em>LÀM THÀNH VIÊN</em> NGAY!
            </h2>
            <p>
              Đăng ký tài khoản trung tâm chúng tôi, bạn sẽ có cơ hội được
              chuyên gia tư vấn và tập những khóa học phù hợp với bản thân
            </p>
            <MButton
              :text="'Đăng ký thành viên ngay'"
              class="button-addtocard"
              :class="'content-top-btn-content'"
            />
          </router-link>
          <router-link
            to="/courses"
            class="donthink-content"
            v-if="isAuthenticated"
          >
            <h2 class="donthink-header">
              CÙNG NHAU <em>LUYỆN TẬP</em>, XÂY DỰNG CƠ THỂ
              <em>MONG ƯỚC</em> NGAY!
            </h2>
            <p>
              Đăng ký khóa học tại trung tâm chúng tôi, bạn sẽ có cơ hội được
              chuyên gia tư vấn và tập những khóa học phù hợp với bản thân
            </p>
            <MButton
              :text="'Đăng ký khóa học ngay'"
              class="button-addtocard"
              :class="'content-top-btn-content'"
            />
          </router-link>
        </div>
      </div>
    </section>
    <div class="sale-course w3-container w3-center mb-4" id="team">
      <h2 class="sale-course-title">KHÓA HỌC MỚI NHẤT TRONG THÁNG</h2>
      <p>Đăng ký ngay trước khi khóa học kết thúc</p>
      <div class="list-course">
        <card-course
          v-for="course in lstCourses"
          :key="course.id"
          :class="['ma-5', selectedClass]"
          :course="course"
        />
      </div>
    </div>
    <section class="services-section">
      <div class="services-contrainer">
        <div class="services-row">
          <div class="service-block col-lg-4 col-md-6 col-sm-12">
            <div class="inner-box">
              <div class="icon-box">
                <i class="ti ti-clock-filled"></i>
              </div>
              <h3 class="label-box">Phát triển cơ bắp hiệu quả</h3>
              <div class="services-des">
                Các bài tập, thiết bị giúp người học có thể phát triển cơ bắp một cách hiệu quả, nhanh chóng
              </div>
            </div>
          </div>
          <div class="service-block col-lg-4 col-md-6 col-sm-12">
            <div class="inner-box">
              <div class="icon-box center">
                <i class="ti ti-barbell"></i>
              </div>
              <h3 class="label-box center">Bài tập phù hợp với cơ thể</h3>
              <div class="services-des center">
                Các bài tập phù hợp với nhóm người tập nhất định, giúp họ phát triển dần dần
              </div>
            </div>
          </div>
          <div class="service-block col-lg-4 col-md-6 col-sm-12">
            <div class="inner-box">
              <div class="icon-box">
                <i class="ti ti-checkup-list"></i>
              </div>
              <h3 class="label-box">Chế độ luyện tập hợp lý</h3>
              <div class="services-des">
                Chế độ luyện tập được nghiên cứu và thử nghiệm giúp người tập phù hợp hơn
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="caculator">
      <div class="caculator-contrainer">
        <div class="bmi-header">TÍNH TOÁN CHỈ SỐ BMI CỦA BẠN</div>
        <div class="bmi-body">
          <div class="bmi-left">
            <div class="input-height">
              <MInput
                class="popup-input"
                :style="'min-height: 60px;'"
                :maxlength="5"
                :typeInput="'number'"
                :placeholder="'Nhập chiều cao của bạn (mét) '"
                v-model="this.guestHeight"
              />
            </div>
            <div class="input-weight">
              <MInput
                class="popup-input"
                :style="'min-height: 60px;'"
                :maxlength="5"
                :typeInput="'number'"
                :placeholder="'Nhập cân nặng của bạn (kg) '"
                v-model="this.guestWeight"
              />
            </div>
            <div class="left-button">
              <MButton
                :text="'Tính chỉ số BMI'"
                :style="'min-width: 200px;min-height: 50px;'"
                @click="calcBMI"
                class="button-calc"
                :class="'content-top-btn-content'"
              />
            </div>
            <div class="left-bmi">
              <label>Chỉ số BMI của bạn là: {{ this.guestBMI }}</label>
              <label>Tình trạng cân nặng: {{ formatBMI(this.guestBMI) }}</label>
            </div>
          </div>
          <div class="bmi-right">
            <table>
              <thead>
                <tr class="pf-bmi-heading">
                  <th class="pt-class">BMI</th>
                  <th>TÌNH TRẠNG CƠ THỂ</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>Dưới 18.5</td>
                  <td>Cân nặng thấp</td>
                </tr>
                <tr>
                  <td>18.5 - 24.9</td>
                  <td>Cân đối</td>
                </tr>
                <tr>
                  <td>25.0 - 29.9</td>
                  <td>Tiền béo phì</td>
                </tr>
                <tr>
                  <td>30.0 trở lên</td>
                  <td>Béo phì</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>
    <!-- Team Container -->
    <div class="ptlist w3-container w3-padding-64 w3-center mb-4" id="team">
      <h2 class="sale-course-title">HUẤN LUYỆN VIÊN TRUNG TÂM</h2>
      <p>Các huấn luyện viên nổi bật</p>

      <div class="ptlist-content">
        <div class="list-pt">
          <div class="pt-avatar" v-for="pt in PTList" :key="pt.id">
            <router-link style="text-decoration: none" :to="`/ptpage/${pt.id}`">
              <MTooltip :content="'Click để xem'" />
              <img class="avatar" :src="pt.avatar" v-if="pt.avatar" />
              <img class="avatar" src="@\assets\photos\pt1.jpg" v-else />
              <div class="pt-name">
                <label>{{ pt.fullName }}</label>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </div>
    <section class="NewsBlog">
      <div class="newsblog-container">
        <h2 class="sale-course-title">BÀI VIẾT MỚI NHẤT</h2>
        <div class="newsblog-content">
          <div class="list-course">
            <card-blog
              v-for="blog in listBlog"
              :key="blog.id"
              :class="['ma-5', selectedClass]"
              :blog="blog"
            />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import MInput from "@/components/MInput/MInput.vue";
import MButton from "@/components/MButton/MButton.vue";
import CardCourse from "@/components/MCard/CardCourse.vue";
import CardBlog from "@/components/MCard/CardBlog.vue";
import fetchOptions from "@/api/base/fetchOptions";
import apiConfig from "@/api/config/apiConfig";
import fireStorage from "@/plugins/fireStorage";
import {
  IS_AUTHENTICATE_GETTER,
  GET_BRANCH_HOME_SELECTED,
} from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "HomePage",
  components: {
    CardCourse,
    CardBlog,
    MButton,
    MInput,
  },
  data() {
    return {
      guestHeight: null,
      guestWeight: null,
      guestBMI: 0,
      senderId: "",
      receiveId: "",
      showChat: false,
      colors: [
        "white",
        "warning",
        "pink darken-2",
        "red lighten-1",
        "deep-purple accent-4",
      ],
      slides: ["First", "Second", "Third", "Fourth", "Fifth"],
      isRole: null,
      PTList: [],
      UserList: [],
      Branch: {},
      urlImage: null,
      listBlog: [],
      lstCourses: [],
      ListType: [],
      iconTypeLeft: [
        "ti ti-heartbeat",
        "ti ti-stretching",
        "ti ti-accessible-off-filled",
      ],
      iconTypeRight: [
        "ti ti-recharging",
        "ti ti-barbell",
        "ti ti-brand-telegram",
      ],
      describeTypeLeft: [
        "Đây là một giải pháp hiệu quả để tăng cường sức khỏe và rèn luyện vóc dáng.",
        "Với sự hướng dẫn chuyên nghiệp từ huấn luyện viên, bạn có cơ hội theo dõi và đạt được mục tiêu cá nhân.",
        "Mang lại trải nghiệm tốt nhất cho sức khỏe và thể chất.",
      ],
      describeTypeRight: [
        "Động tác linh hoạt, giúp cải thiện sức khỏe về cả thể chất và tinh thần.",
        "Sự kết hợp của nghệ thuật và thể dục, giúp tăng cường sức mạnh cơ bắp và linh hoạt.",
        "Mang lại niềm vui và năng lượng tích cực, đồng thời khám phá sự đẹp và sức mạnh của cơ thể.",
      ],
    };
  },
  created() {
    (async () => {
      await this.loadAllCourses();
      await this.loadAllTypeCourses();
      await this.loadPT();
      await this.loadBlog();
    })();
  },

  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    async loadAllCourses() {
      this.showLoading = true;
      try {
        let filter = `?branchId=${this.getBranch.id}&size=4`;
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

    async loadAllTypeCourses() {
      this.showLoading = true;
      try {
        let filter = `?page=0&size=6`;
        fetch(`${apiConfig}/course-types${filter}`, await fetchOptions("GET"))
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            this.ListType = response.content;
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

    async loadPT() {
      try {
        fetch(
          `${apiConfig}/pt-courses/mentor?branchId=${this.getBranch.id}&size=4`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.PTList = data.content;
            await this.PTList.map(async (pt) => {
              pt.avatar = await fireStorage.displayImage(pt.avatar);
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
    },

    async loadBlog() {
      this.showLoading = true;
      try {
        const filter = `?branchId=${this.getBranch.id}&size=4`;
        fetch(`${apiConfig}/blogs${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.listBlog = data.content;
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
    calcBMI() {
      if (this.guestHeight && this.guestWeight) {
        this.guestBMI = (
          this.guestWeight /
          (this.guestHeight * this.guestHeight)
        ).toFixed(2);
      }
    },
    formatBMI(bmi) {
      if (bmi == 0) {
        return "";
      } else if (bmi < 18.5) {
        return "CÂN NẶNG THẤP";
      } else if (bmi >= 18.5 && bmi < 25) {
        return "CÂN ĐỐI";
      } else if (bmi >= 25 && bmi < 30) {
        return "TIỀN BÉO PHÌ";
      } else {
        return "BÉO PHÌ";
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
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_HOME_SELECTED,
    }),
    ...mapGetters("auth", {
      isAuthenticated: IS_AUTHENTICATE_GETTER,
    }),
    leftList() {
      try {
        // Lấy 3 phần tử đầu tiên từ danh sách
        return this.ListType.slice(0, 3).map((item, index) => ({
          ...item,
          iconType: this.iconTypeLeft[index],
          description: this.describeTypeLeft[index],
        }));
      } catch (error) {
        console.log("Lỗi hệ thống");
        return [];
      }
    },
    rightList() {
      try {
        // Lấy các phần tử còn lại từ vị trí thứ 3 trở đi
        return this.ListType.slice(3).map((item, index) => ({
          ...item,
          iconType: this.iconTypeRight[index],
          description: this.describeTypeRight[index],
        }));
      } catch (error) {
        console.log("Lỗi hệ thống");
        return [];
      }
    },
  },
  watch: {
    getBranch() {
      this.loadAllCourses();
      this.loadPT();
      this.loadBlog();
    },
  },
};
</script>

<style scoped>
#homepage {
  min-height: 100vh;
}
#btnChat {
  position: fixed;
  right: 2%;
  bottom: 5%;
}
#popupChat {
  position: fixed;
  right: 2%;
  bottom: 5%;
}
.sheet {
  position: relative;
}
.sider-img {
  width: 100%;
  height: 100%;
  position: relative;
}
.card {
  width: 500px;
  position: absolute;
  top: 150px;
  left: 10%;
  background-color: transparent !important;
  color: #fff;
}
.sale-course {
  padding-top: 40px;
  height: 750px;
  padding: 4% 0;
}
.sale-course-title {
  font-weight: bold;
  height: 10%;
}
.sale-course-title em {
  color: #e9485a;
  font-style: normal;
}
.list-course {
  margin-top: 30px;
  height: 80%;
  width: 100%;
  padding: 0 10%;
  display: flex;
  gap: 65px;
  flex-wrap: wrap;
  padding: 0 5%;
  min-height: 50vh;
}
.services-section {
  position: relative;
  background-color: #fff;
  display: block;
  margin: 0px;
  padding: 0px;
  border: none;
  outline: none;
}
.services-section::before {
  position: absolute;
  content: "";
  left: 0%;
  top: 0px;
  width: 35%;
  height: 100%;
  background-color: #222222;
  box-sizing: border-box;
}
.services-section::after {
  position: absolute;
  content: "";
  right: 0%;
  top: 0px;
  width: 35%;
  height: 100%;
  background-color: #ff0000;
  box-sizing: border-box;
}
.services-contrainer {
  position: static;
  max-width: 1400px;
  padding: 0px 15px;
  margin: 0 auto;
}
.service-block {
  position: relative;
  z-index: 1;
  width: 100%;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
  flex: 0 0 33.333333%;
  max-width: 33.333333%;
  transition: transform 0.3s ease-in-out;
}
.inner-box {
  position: relative;
  text-align: center;
  padding: 80px 0px 80px;
}
.icon-box {
  position: relative;
  font-size: 70px;
  line-height: 1em;
  color: #ffffff;
  transition: all 600ms ease;
  -moz-transition: all 600ms ease;
  -webkit-transition: all 600ms ease;
  -ms-transition: all 600ms ease;
  -o-transition: all 600ms ease;
}
.service-block:hover {
  transform: scale(1.1);
}
.icon-box.center {
  color: #222222;
}
.services-des {
  position: relative;
  color: #ffffff;
  font-size: 16px;
  line-height: 1.8em;
  padding: 0px 50px;
  font-family: "Roboto", sans-serif;
}
.services-des.center {
  color: #222222;
}
.services-row {
  display: -ms-flexbox;
  display: flex;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  margin-right: -15px;
  margin-left: -15px;
}
.label-box {
  position: relative;
  color: #ffffff;
  font-weight: bold;
}
.label-box.center {
  color: #ff0000;
}
.newsblog-header {
  font-weight: bold;
  height: 10%;
}
.program {
  width: 100%;
  height: 850px;
  padding: 60px 100px;
}
.program-contrainer {
  width: 100%;
  height: 100%;
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: 60px;
}
.program-row {
  display: flex;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  margin-right: -15px;
  margin-left: -15px;
}
.program-header {
  position: relative;
  width: 100%;
  margin-right: 5%;
  margin-bottom: 50px;
  display: flex;
  align-items: center;
  text-align: center;
  padding: 0 25%;
}

.program-content-left {
  flex: 0 0 50%;
  max-width: 50%;
  padding-left: 15%;
}
.program-item {
  display: flex;
  margin-bottom: 60px;
  padding: 0 5%;
  transition: transform 0.3s ease-in-out;
}
.program-icon {
  font-size: 100px;
  width: 20%;
  height: 100%;
  color: #e9485a;
}
.program-item:hover {
  transform: scale(1.1);
  border: 1px solid #bbbb;
  border-radius: 15px;
  background-color: #fff;
}
.program-right-content {
  width: 70%;
  height: 100%;
  padding: 0 20px;
}
.program-right-content p {
  text-align: left;
}
.program-content-right {
  flex: 0 0 50%;
  max-width: 50%;
  padding-right: 15%;
}
.program-name {
  display: flex;
  font-weight: bold;
}
.program-link {
  width: 100%;
  margin-top: 13px;
  display: inline-block;
  font-size: 13px;
  text-transform: uppercase;
  color: #e9485a;
  font-weight: 500;
  text-align: left;
  cursor: pointer;
  text-decoration: none;
}
.donthink {
  padding: 120px 0px;
  background-image: url(../../assets/photos/backgroundimg1.jpg);
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
  text-align: center;
  transition: transform 0.3s ease-in-out;
}
.donthink:hover {
  transform: scale(1.01); /* Độ phóng to khi di chuột vào */
}
.donthink-contrainer {
  max-width: 1300px;
  width: 100%;
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}
.donthink-row {
  display: flex;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  margin-right: -15px;
  margin-left: -15px;
}
.donthink-content {
  flex: 0 0 83.333333%;
  max-width: 83.333333%;
  margin-left: 10%;
}
.donthink-header {
  width: 100%;
  font-size: 32px;
  text-transform: uppercase;
  font-weight: 800;
  color: #fff;
  letter-spacing: 1px;
}
.donthink-header em {
  font-style: normal;
  color: #e9485a;
}
.donthink-content p {
  font-size: 16px;
  color: #fff;
  margin: 35px 0px 35px 0px;
}
.ptlist {
  height: 800px;
  padding: 120px 0px;
  background-color: #222222;
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
  text-align: center;
  color: #fff;
}
.ptlist-content {
  width: 100%;
  height: 80%;
}
.pt-avatar {
  width: 300px;
  height: 400px;
  cursor: pointer;
  position: relative;
  background-color: #ccced1;
}
.list-pt {
  display: flex;
  gap: 100px 65px;
  flex-wrap: wrap;
  padding: 3% 7%;
}
.avatar {
  width: 100%;
  height: 100%;
  transition: 0.5s ease;
  backface-visibility: hidden;
  opacity: 1;
  display: block;
}
.pt-name {
  margin-top: 20px;
  font-size: 20px;
  color: #fff;
}

.pt-avatar:hover .avatar {
  opacity: 0.3;
}
.caculator {
  height: 800px;
  position: relative;
  padding: 30px 0px;
}
.caculator-contrainer {
  max-width: 1300px;
  width: 100%;
  height: 100%;
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
  display: flex;
  flex-wrap: wrap;
  padding: 8% 0;
}
.bmi-header {
  font-size: 38px;
  font-weight: bold;
  line-height: 64px;
  z-index: 9;
  position: relative;
  margin-bottom: 20px;
  letter-spacing: 1px;
  width: 100%;
  height: 15%;
}
.bmi-body {
  height: 85%;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
}
.bmi-left {
  height: 100%;
  width: 50%;
  padding: 0 3%;
}

.input-height {
  width: 50%;
  text-align: left;
  height: 25%;
  font-size: 18px;
}
.input-weight {
  width: 50%;
  height: 25%;
  font-size: 18px;
}
.left-button {
  height: 20%;
  width: 40%;
  margin-left: 0;
  padding-left: 0;
  display: flex;
  justify-content: left;
}

.left-bmi {
  font-weight: bold;
  text-align: left;
  display: flex;
  flex-wrap: wrap;
  line-height: 3;
}
.left-bmi label {
  width: 100%;
  font-size: 16px;
}

.bmi-right {
  height: 100%;
  width: 50%;
  padding: 0 3%;
  display: flex;
  justify-content: right;
  padding-bottom: 20px;
}
.bmi-right table {
  height: 100%;
  width: 100%;
  border: solid 1px black;
  font-weight: bold;
}
table tr {
  height: 70px;
}

.bmi-right thead th {
  background-color: #222222;
  font-weight: bold;
  text-align: center;
  color: #ff0000;
  font-size: 16px;
}
.bmi-right tbody td {
  border: 1px solid #ccc;
}
.w3-display-container {
  height: 600px;
}
.NewsBlog {
  display: block;
  padding: 4% 0px;
  height: 800px;
}
@media (max-width: 1200px) {
  .w3-display-container {
    width: 100%;
    height: 200px;
  }
  .v-window {
    height: 200px;
  }
  .title {
    display: none;
  }
  .program {
    padding: 0;
    height: 400px;
  }
  .program-contrainer {
    margin: 0;
    padding: 20px 0;
  }
  .program-header {
    padding: 0;
    margin-right: 0;
    margin-bottom: 30px;
    display: block;
    margin-left: 20px;
  }
  .program-content-left {
    padding-left: 5px;
  }
  .program-icon {
    font-size: 40px;
    width: 30%;
  }
  .program-name {
    font-size: 16px;
    height: 22px;
    overflow: hidden;
  }
  .program-right-content p {
    display: none;
  }
  .program-content-right {
    padding: 0;
  }
  .program-right-content {
    padding: 0;
    width: 80%;
  }
  .program-link {
    margin: 0;
    font-size: 10px;
  }
  .program-item {
    margin-bottom: 30px;
  }
  .program-row {
    width: 100%;
    margin: 0;
  }
  .program-item:focus {
    transform: scale(1.1);
    border: 1px solid #bbbb;
    border-radius: 15px;
    background-color: #fff;
  }
  .donthink {
    width: 100%;
    height: 250px;
    padding: 0;
  }
  .donthink-contrainer {
    padding: 0;
  }
  .donthink-content {
    max-width: 100%;
    margin-left: 4%;
    flex: 0 0 100%;
  }
  .donthink-header {
    font-size: 20px;
    padding: 0 10px;
    line-height: 40px;
  }
  .donthink-content p {
    font-size: 13px;
    margin: 23px 26px 23px 20px;
  }
  .donthink-row {
    width: 100%;
  }

  .sale-course {
    width: 100%;
    height: 100%;
    padding: 0;
  }
  .sale-course-title {
    font-size: 18px;
  }
  .services-section {
    width: 100%;
  }
  .services-des {
    display: none;
  }
  .label-box {
    font-size: 16px;
  }

  .caculator {
    width: 100%;
  }
  .ptlist {
    height: 100%;
    width: 100%;
  }
  .list-course {
    padding: 0 30px;
    gap: 0;
  }
  .v-slide-group__content {
    display: block !important;
  }
  .NewsBlog {
    height: 100%;
  }
  .bmi-body{
    display: block;
  }
  .bmi-left{
    width: 100%;
    height: 400px;
    padding: 0 10%;
  }.input-height, .input-weight{
    width: 100%;
    height: 24%;
  }.left-button{
    display: block;
    width: 100%;
    justify-self: center;
  }.caculator-contrainer{
    padding: 0;
  }.caculator{
    height: 100%;
  }.bmi-right{
    width: 100%;
  }
}
@media (min-width: 1580px) {
  .list-course {
    gap: 25px;
    padding: 0 4%;
  }
}
@media (min-width: 1535px) and (max-width: 1600px) {
  .list-course * {
    width: 18%;
    display: flex;
    flex-wrap: wrap;
    margin-right: 1%;
    height: 65%;
  }
  .pt-avatar {
    width: 20%;
    display: flex;
    flex-wrap: wrap;
    margin-right: 1%;
  }
}
</style>
