<template>
  <div class="header">
    <div class="header-left">
      <div class="header-left-drop" v-if="role == 'ADMIN'">
        <MDropItems
          v-if="isDropItems"
          :MDropSta="3"
          :Inputstyle="'border: none; height: 40px; background-color: #fff'"
          :Drstyle="'margin-top: 15px; width: 250px; max-height: 250px'"
          :label="'name'"
          :url="'logoUrl'"
          :DropItemsModel="selectedBranch.id"
          :RecordDrop="Branches"
          @dropitem-value="getBranch"
        />
      </div>
    </div>
    <div class="header-right">
      <div class="header-avatar">
        <img class="header-avatar-img" src="@\assets\photos\avatar.png" />
      </div>
      <label class="header-username">{{ fullName }}</label>
      <button
        type="button"
        class="header-right-btn header-right-btn-down"
        :style="style"
      >
        <MDropItems
          :MDropSta="1"
          :Inputstyle="'width: 30px; height: 30px; margin-top: -4px; margin-left: 13px; background: var(--icon) no-repeat -371px -364px;'"
          :Drstyle="'top:25px; right:-17px;display:flex;'"
          :RecordDrop="SystemAccess"
          @dropitem-value="Logout"
          :label="'name'"
        />
      </button>
    </div>
  </div>
</template>

<script>
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import apiConfig from "@/api/config/apiConfig";
import fireStorage from "@/plugins/fireStorage";
import fetchOptions from "@/api/base/fetchOptions";
import { mapMutations } from "vuex";
import { SET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { LOGOUT_ACTION } from "@/stores/modules/storeconstants";
import { mapActions } from "vuex";
export default {
  name: "TheHeaderAdmin",
  components: {
    MDropItems,
  },
  created() {
    this.role = JSON.parse(localStorage.getItem("userData"))["role"];
    this.selectedBranch.id = JSON.parse(localStorage.getItem("userData"))[
      "branchId"
    ];
    if (this.role == "ADMIN") {
      this.selectedBranch.id = localStorage.getItem("branchIdGuest");
    }
    this.setBranch({ id: this.selectedBranch.id });
    (async () => {
      await this.loadData();
      //lấy dữ liệu và tên người đăng nhập
        this.fullName = JSON.parse(localStorage.getItem("userData"))[
          "username"
        ];
    })();
  },

  mounted() {
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  methods: {
    ...mapMutations({
      setBranch: SET_BRANCH_SELECTED,
    }),
    ...mapActions("auth", {
      logout: LOGOUT_ACTION,
    }),

    /**
     * Hàm click outside sẽ đóng dropbox
     */
    clickEventInterrupt(event) {
      try {
        if (this.showNotification == true) {
          const isClick = this.$refs.notification.contains(event.target);
          if (!isClick) {
            this.showNotification = false;
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * Hàm lấy branch
     */
    getBranch(branchid) {
      console.log(branchid);
      this.selectedBranch.id = branchid;
      this.setBranch({ id: branchid });
      if (this.role == "ADMIN") {
        localStorage.setItem("branchIdGuest", branchid);
      }
    },
    /**
     * Hàm đăng xuất
     */
    Logout() {
      this.logout();
      var rememberMe = localStorage.getItem("rememberMe");
      if (rememberMe === "false") {
        localStorage.removeItem("userName");
        localStorage.removeItem("password");
        localStorage.removeItem("rememberMe");
        if (this.role == "ADMIN") {
          localStorage.removeItem("branchIdGuest");
        }
      }
    },
    openDropSystemAccess() {
      this.isDropSystemAccess = true;
      this.Rotate();
    },
    /**
    hàm xoay icon nút
    */
    Rotate() {
      if (this.OpenDropboxD == true) {
        this.style = "transition: linear 0.2s; transform: rotate(180deg);";
      } else {
        this.style = "transition: linear 0.2s; transform: rotate(0deg);";
      }
    },

    /**
     * hàm load dữ liệu
     */
    async loadData() {
      try {
        fetch(`${apiConfig}/branches`, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then(async (data) => {
          this.Branches = data.content; //lưu dữ liệu
          this.Branches.unshift({
            id: "",
            name: "Tất cả",
            // ...Thêm các trường khác nếu cần
          });
          await this.Branches.map(async (branch) => {
             branch.logoUrl = await fireStorage.displayImage(branch.logoUrl);
            });
          this.isDropItems = true;
          if (this.Branches.length > 0 && !this.selectedBranch.id) {
            this.selectedBranch.id = this.Branches[0].id;
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
  },
  data() {
    return {
      showNotification: false,
      Branches: [],
      selectedBranch: {
        id: null,
      },
      isDropItems: false,
      isDropSystemAccess: false,
      SystemAccess: [{ name: "Đăng xuất", id: 1 }],
      fullName: "",
      role: null,
    };
  },
};
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.header-left {
  width: calc(100%-300px);
  height: 56px;
  display: flex;
  align-items: center;
  flex-direction: row;
  float: left;
  column-gap: 10px;
}
.header-left-btn {
  width: 24px;
  height: 24px;
  border: none;
}
.header-left-btn-menu {
  background-image: var(--icon);
  background-repeat: no-repeat;
  background-position: -845px -32px;
  background-color: #fff;
}
.header-left-btn-down {
  background-image: var(--icon);
  background-repeat: no-repeat;
  background-position: -943px -357px;
  background-color: #fff;
}
.header-right {
  width: 250px;
  height: 56px;
  display: flex;
  float: right;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  margin-right: 30px;
}
.header-right-btn:hover {
  background-color: #f1f1f1;
  cursor: pointer;
}
.header-notification:hover .header-tooltip {
  visibility: visible;
}
.header-notification {
  background-color: transparent;
  position: relative;
}
.card-notification {
  position: absolute;
  top: 100%;
  right: 10%;
  z-index: 3;
  width: max-content;
}
.list-notification {
  max-height: 200px;
  overflow-y: auto;
}
.list-notification {
  text-align: start;
}
.ti-bell {
  font-size: 1.6em;
}

.header-tooltip {
  width: fit-content;
  font-size: 0.7em;
  left: -5%;
  top: 120%;
  position: absolute;
  background-color: #505050;
  border-radius: 2px;
  padding: 5px;
  z-index: 3;
  text-align: center;
  color: #fff;
  white-space: nowrap;
  visibility: hidden;
}
.header-notification::after {
  content: "";
  position: absolute;
  background-color: red;
  height: 8px;
  width: 8px;
  border-radius: 100%;
  top: 20%;
  left: 55%;
}
.header-avatar {
  width: 37px;
  height: 32px;
  border-radius: 50%;
  margin-left: 20px;

  background: var(--icon) no-repeat -353px -893px;
}
.header-avatar-img {
  width: 30px;
  height: 30px;
}
.header-right-btn-down {
  background: var(--icon) no-repeat -943px -357px;
}
.header-username {
  max-width: 150px;
  font-weight: bolder;
  font-size: medium;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.header-left-label {
  font-weight: bold;
}
.header-left-drop {
  width: 180px;
  height: 100%;
  padding-top: 8px;
}
</style>
