<template>
  <div class="body">
    <div class="body-contain">
      <div class="search-contain">
        <div class="clb-count">
          <h1 v-if="this.listBranch">
            {{ this.listBranch.length }}+ CHI NHÁNH
          </h1>
          <h1 v-if="!this.listBranch">CÁC CHI NHÁNH</h1>
        </div>
        <div class="search-position">
          <div class="search-group">
            <div class="search-box" @click="toggleBranch">
              <img
                id="icon-search"
                style="object-fit: contain; padding-left: 20px"
                alt="icon"
                src="https://cali.vn/themes/cfyc//assets/static/icon/search.svg"
              />
              <input
                type="text"
                placeholder="Chọn chi nhánh của bạn.."
                autocomplete="off"
                id="myInput"
              />
              <div
                class="drop-box-mobile"
                id="myDropdown"
                :class="{ show: isShowDropdownBranch }"
              >
                <div id="myDropdown_inside" class="search-clb-dropdown-content">
                  <a v-for="branch in this.listBranch" :key="branch.id">
                    <label>{{ branch.name }} </label>
                  </a>
                </div>
              </div>
              <img
                class="search-arrow"
                :class="{ rotate: isArrowUp }"
                id="icon-search-dropdown"
                style="margin-right: 20px"
                alt="Vector-submenu.svg"
                src="https://cali.vn/themes/cfyc/assets/static/icon/Vector-submenu.svg"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="list-item-contain">
        <div
          class="clb-item"
          v-for="branch in this.listBranch"
          :key="branch.id"
        >
          <div class="clb-left-contain">
            <div class="clb-left-content">
              <div class="item-title">
                <h2>{{ branch.name }}</h2>
              </div>
              <div class="description">
                <p>{{ branch.description }}</p>
              </div>
            </div>
            <div class="explore-more">
              <a
                href="https://maps.app.goo.gl/mLyuD3be1vPun3L9A"
                class="clb-link"
                >Xem phòng tập {{ branch.name }}</a
              >
            </div>
          </div>
          <div class="clb-right-contain city-club-thumbnail">
            <v-carousel :show-arrows="false" hide-delimiters>
              <v-carousel-item
                :src="branch.backgroundUrl"
                cover
              ></v-carousel-item>
            </v-carousel>
          </div>
        </div>
      </div>
    </div>
    <div class="white-contain">
      <div class="quest-contain">
        <h3 dir="ltr">
          <span style="font-size: 30px"
            ><strong>CÁC CÂU HỎI THƯỜNG GẶP</strong></span
          >
        </h3>
        <p><br /></p>
        <p dir="ltr">
          <span style="font-size: 18px"
            ><strong
              >Hội viên có thể tập luyện ở các phòng tập khác nơi mình đăng ký
              lúc đầu hay không?</strong
            ></span
          >
        </p>
        <p dir="ltr">
          <span style="font-size: 18px"
            > Gym Management System luôn cố gắng tạo điều kiện để mang lại sự tiện
            lợi và dịch vụ tốt nhất cho hội viên. Vì vậy, hội viên từ các chi
            nhánh khác nhau đều có thể tham gia hầu hết các phòng tập trong hệ
            thống.</span
          >
        </p>
        <p>
          <span style="font-size: 18px"><br /></span>
        </p>
        <p dir="ltr">
          <span style="font-size: 18px"
            ><strong
              >Trang thiết bị ở các cơ sở có giống nhau hay không?</strong
            ></span
          >
        </p>
        <p dir="ltr">
          <span style="font-size: 18px"
            >Về cơ bản, hệ thống phòng tập Gym Management System đều được
            trang bị đầy đủ các thiết bị, dụng cụ với những khu vực tập luyện
            riêng biệt. Tuy nhiên, ở mỗi trung tâm sẽ có những thiết kế riêng
            đáp ứng những nhu cầu khác nhau của khách hàng nhằm tạo ra những
            trải nghiệm mới lạ và không trùng lặp.</span
          >
        </p>
        <p>
          <span style="font-size: 18px"><br /></span>
        </p>
        <p dir="ltr">
          <span style="font-size: 18px"
            ><strong
              >Tôi có thể được giải đáp thắc mắc về hệ thống ở đâu?</strong
            ></span
          >
        </p>
        <p dir="ltr">
          <span style="font-size: 18px"
            >Bên cạnh những lớp học đa dạng,
            <span style="font-size: 18px">Gym Management System</span>
            luôn có các chuyên viên túc trực 24/7 để giải đáp mọi thắc mắc của bạn, 
            giúp bạn hiểu rõ hơn về hệ thống và dễ dàng đăng kí tập luyện tại các 
            chi nhánh trên toàn quốc</span
          >
        </p>
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
export default {
  components: {},
  created() {
    this.loadBranch();
  },

  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    toggleBranch() {
      this.isShowDropdownBranch = !this.isShowDropdownBranch;
      this.isArrowUp = !this.isArrowUp;
    },

    async loadBranch() {
      try {
        fetch(`${apiConfig}/branches`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.listBranch = data.content;
            this.listBranch.map(async (branch) => {
              branch.backgroundUrl = await fireStorage.displayImage(
                branch.backgroundUrl
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
  data() {
    return {
      isShowDropdownBranch: false,
      isArrowUp: false,
      listBranch: [],
    };
  },
};
</script>

<style scoped>
html,
.body {
  overflow-x: hidden;
}
.body {
  line-height: 1.5;
  color: #212529;
  text-align: left;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
  padding: 5% 8%;
}
.body-contain {
  border-radius: 10px;
  background-color: #fff;
}
.search-contain,
.list-item-contain {
  max-width: 1300px;
  margin: auto;
  padding: 80px 0;
}
.search-contain {
  padding-bottom: 0px;
}

.clb-count h1 {
  font-family: monospace;
  font-style: normal;
  font-weight: 600;
  font-size: 60px;
  line-height: 90px;
  /* margin-top: 80px; */
  color: #000;
  margin-bottom: 40px;
}
.clb-item:first-child {
  border-top: 1px solid rgb(216, 211, 211);
}
.clb-item {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 40px 0;
  border-bottom: 1px solid rgb(216, 211, 211);
  max-height: 600px;
  max-height: 600px;
  transition: transform 0.3s ease-in-out;
}
.clb-item:hover {
  transform: scale(1.05);
}

.clb-left-contain {
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: unset;
  padding-right: 150px;
}
.clb-left-contain .clb-left-content .item-title,
.clb-left-contain .clb-left-content .description {
  width: 100%;
}

.clb-left-contain .explore-more a {
  color: #000 !important;
  margin-right: 10px;
  font-style: normal;
  font-size: 16px;
  line-height: 17px;
  font-weight: normal;
}
.clb-link {
  width: 100%;
}
.explore-more p {
  margin: 20px 0;
  font-size: 15px;
  width: 100%;
  max-height: 200px;
  overflow: hidden;
}
.clb-left-contain .explore-more a:hover {
  text-decoration: none;
  color: unset;
}
.clb-left-contain .explore-more img {
  transition: transform 0.1s ease-in-out, -webkit-transform 0.1s ease-in-out;
}
.clb-left-contain .explore-more:hover img {
  transform: translate(5px, 0);
}
.clb-left-content .item-title {
  margin-bottom: 8px;
}
.clb-left-content .description {
  margin-bottom: 16px;
}
.clb-left-content .information-content {
  margin-bottom: 22px;
}
.clb-left-content .information-content .address {
  margin-bottom: 0px;
  font-family: Montserrat;
  font-style: normal;
  font-weight: normal;
  font-size: 14px;
  line-height: 21px;
  color: #000;
}
.clb-left-content .information-content .hot-line {
  margin-bottom: 23px;
  font-family: Montserrat;
  font-style: normal;
  font-weight: normal;
  font-size: 14px;
  line-height: 21px;
  color: #000;
}

.clb-left-content .information-content .opening-hours {
  margin: 0;
  font-family: Montserrat;
  font-style: normal;
  font-weight: normal;
  font-size: 14px;
  line-height: 21px;
  color: #000;
}
.clb-left-contain .btn-submit .submit-trial {
  background: #da2128;
  box-sizing: border-box;
  border-radius: 0;
  max-width: 180px;
  margin-right: 0;
  color: #000;
  padding: 11px 30px;
  font-size: 15px;
  line-height: 18px;
  font-family: Montserrat;
  font-style: normal;
  font-weight: bold;
  text-align: center;
  letter-spacing: -0.015em;
}
.clb-left-contain .btn-submit .submit-trial:hover {
  text-decoration: unset;
  background-color: #000;
}
.clb-left-contain .explore-contain {
  display: flex;
  align-items: flex-end;
  flex-direction: row;
  justify-content: space-between;
}
.clb-left-contain .explore-contain .btn-submit a:hover {
  text-decoration: none;
}
.clb-left-contain .explore-contain .btn-submit a button:focus {
  outline: none !important;
}
.clb-right-contain {
  width: 85%;
  height: 100%;
  margin-top: 24px;
  overflow: hidden;
  position: relative;
}

.clb-right-contain img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}
.item-title h2 {
  font-family: monospace;
  font-style: normal;
  font-weight: 500;
  font-size: 34px;
  line-height: 60px;
  width: 80%;
  color: #000;
}
.clb-item .description {
  padding-top: 30px;
  font-weight: normal;
  font-size: 14px;
  line-height: 1.5;
  width: 80%;
  color: #000;
}

#myInput {
  background-position: 14px 12px;
  background-repeat: no-repeat;
  font-size: 16px;
  padding: 14px 20px 12px 10px;
  font-family: Montserrat;
  font-style: normal;
  font-weight: normal;
  font-size: 16px;
  line-height: 20px;
  display: flex;
  align-items: center;
  color: #141414;
  width: 100%;
}
.v-window {
  height: 400px !important;
}

#myInput:focus {
  outline: unset;
}

.search-clb-dropdown {
  position: relative;
  display: inline-block;
}
.drop-box-mobile {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  align-items: center;
  justify-content: center;
}
.search-clb-dropdown-content {
  position: absolute;
  background-color: #fff;
  min-width: 230px;
  overflow: auto;
  margin-top: 47px;
  z-index: 5;
  width: 400px;
  max-height: 192px;
}

.search-box-district {
  display: flex;
  width: 100%;
  position: relative;
}
.search-clb-dropdown-content a {
  padding: 12px 24px;
  font-size: 16px;
  line-height: 24px;
  width: 100%;
  max-height: 48px;
  color: #141414;
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}
.search-clb-dropdown-content a label {
  cursor: pointer;
}
.search-box {
  display: flex;
  width: 100%;
  position: relative;
}
.show {
  display: block;
}
.search-clb-dropdown-content a:hover {
  background: #e1e1e1;
  color: #141414;
  text-decoration: unset !important;
}

.search-group {
  display: flex;
  flex-direction: row;
  width: 400px;
  justify-content: space-between;
  border: 1px solid rgb(216, 211, 211);
  background: #fff;
}
.search-group form {
  width: 100%;
}
.quest-contain {
  max-width: 1200px;
  margin: auto;
  padding: 80px 0;
  line-height: 1.5;
}
.rotate {
  transform: rotate(180deg);
}
.search-arrow {
  transition: transform 0.3s ease;
}
.white-contain {
  background-color: #fff;
}
.branch-img {
  height: 100%;
  width: 100%;
  max-height: 496px;
}
@media (max-width: 1200px) {
  .body {
    width: 100%;
    padding: 5%;
  }
  .body-contain {
    padding: 3%;
  }
  .search-contain {
    padding: 0;
  }
  .clb-count h1 {
    font-size: 50px;
  }
  .search-group {
    width: 100%;
  }
  .list-item-contain {
    padding: 0;
  }
  .clb-item {
    display: block;
    max-height: 100%;
  }
  .clb-left-contain {
    width: 100%;
    padding: 0;
  }
  .clb-right-contain {
    width: 100%;
  }
  .description {
    padding-top: 8px;
    max-height: 200px;
    overflow: hidden;
  }
  .item-title h2 {
    width: 100%;
  }
}
</style>