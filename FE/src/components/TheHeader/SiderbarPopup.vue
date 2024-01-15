<template>
  <div class="siderbar-popup">
    <div class="siderbar-popup-header">
        <button type="button" class="btn-close" @click="closeSiderbar">
            <i class="ti ti-x"></i>
        </button>
    </div>
    <div class="search-box">
        <MInputSearch @InputWhere="getSearchUser"
              :placeholder="'Tìm khóa học theo tên'"
              />
    </div>
    <div class="text-start">
        <ul class="offcanvas_main_menu">
            <li class="menu-item-has-children" v-if="isAuthenticated">
                <router-link to="/profile" @click="closeSiderbar" class="">Tài khoản của tôi</router-link>
            </li>
            <li class="menu-item-has-children" v-if="isAuthenticated">
                <router-link to="/cartpage" @click="closeSiderbar" class="">Đăng ký của tôi</router-link>
            </li>
            <li class="menu-item-has-children" v-if="roleValue == 'USER'">
                <router-link to="/usercourse" @click="closeSiderbar" class="">Quản lý lịch tập</router-link>
            </li>
            <li class="menu-item-has-children" v-if="roleValue == 'USER'">
                <router-link to="/orderuser" @click="closeSiderbar" class="">Lịch sử đăng ký</router-link>
            </li>
            <li class="menu-item-has-children" v-if="!isAuthenticated">
                <router-link to="/login" @click="closeSiderbar" class="">Đăng nhập</router-link>
            </li>
            <li class="menu-item-has-children" v-if="isAuthenticated" @click="toLogout">
                <router-link to="/home" @click="closeSiderbar" class="">Đăng xuất</router-link>
            </li>
        </ul>
    </div>
  </div>
</template>

<script>
import MInputSearch from "@/components/MInput/MInputSearch.vue";
export default {
    name: "SiderbarPopup",
  components: {
    MInputSearch,
  },
  props:{
    roleValue: String,
    isAuthenticated: Boolean,
  },
  methods:{
    closeSiderbar(){
        this.$emit("close-Sider-bar");
    },
    toLogout(){
        this.$emit("to-logout");
    }
  },
  data(){
    return{
        

    }
  }
}
</script>

<style scoped>
.siderbar-popup{
position: absolute;
    height: 100vh;
    width: 100%;
    top: 0;
    -webkit-box-pack: start;
    -ms-flex-pack: start;
    justify-content: flex-start;
    background: #000;
    right: 0;
    padding: 20px 20px;
    display: block;
    z-index: 99;
    -webkit-transition: 0.3s;
    transition: 0.3s;
}
.siderbar-popup-header{
    display: flex;
    align-items: center;
    justify-content: flex-end!important;
    padding: 1rem 1rem;
}
.btn-close{
    padding: .5rem .5rem;
    margin-top: -.5rem;
    margin-right: -.5rem;
    margin-bottom: -.5rem;
    font-size: 30px;
}
.search-box{
    padding-right: 0;
    margin-bottom: 20px;
    color: #fff;
}
ul {
    list-style-image: none;
    list-style: outside none none;
    margin: 0;
    padding: 0;
}
.offcanvas_main_menu li {
    position: relative;
}.offcanvas_main_menu li a {
    border-bottom-color: rgb(28, 34, 36);
}
.offcanvas_main_menu li a {
    color: #fff;
    text-decoration: none;
    font-size: 14px;
    font-weight: 400;
    text-transform: capitalize;
    display: block;
    padding-bottom: 10px;
    margin-bottom: 10px;
    border-bottom: 1px solid #ebebeb;
}
</style>