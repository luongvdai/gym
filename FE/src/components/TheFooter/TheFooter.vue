<template>
  <!-- <div class="padding">
    <div class="padding-left" v-if="TotalCount">
      <label>Tổng số: </label>
      <label class="padding-left-bold">{{ TotalCount.totalCount }}</label>
      <label> bản ghi</label>
    </div>
  
  </div> -->
  <div class="footer">
    <div class="footer-row-top">
      <div class="footer-col">
        <div class="footer-col-left">
          <label class="footer-company-name">GSM</label>
          <label class="footer-company-des">
            With the product "Gym management system" will help improve the
            management and experience of both managers and students in gyms
          </label>
        </div>
      </div>

      <div class="footer-col">
        <div class="footer-about-us">
          <label class="footer-head">About us</label>
          <router-link class="footer-item" to="/">Privacy</router-link>
          <router-link class="footer-item" to="/">Team of use</router-link>
          <router-link class="footer-item" to="/">Customercode</router-link>
        </div>
      </div>

      <div class="footer-col b">
        <div class="footer-map">
          <label class="footer-head">Address</label>
          <iframe
            class="iframe-map"
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.49114777
            79788!2d105.52392847500008!3d21.013025188319308!2m3!1f0!2f0!3f0!3m2!1i102
            4!2i768!4f13.1!3m3!1m2!1s0x3135abc60e7d3f19%3A0x2be9d7d0b5abcbf4!2zVHLGsOG
            7nW5nIMSQ4bqhaSBI4buNYyBGUFQ!5e0!3m2!1svi!2s!4v1696564788260!5m2!1svi!2s"
            style="border: 0"
            allowfullscreen=""
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade"
          ></iframe>
        </div>
      </div>

      <div class="footer-col b">
        <div class="footer-right">
          <label class="footer-head">Contact</label>
          <div class="footer-right-item">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="icon icon-tabler icon-tabler-mail"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              stroke-width="2"
              stroke="currentColor"
              fill="none"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
              <path
                d="M3 7a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v10a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-10z"
              ></path>
              <path d="M3 7l9 6l9 -6"></path>
            </svg>
            <label class="footer-right-label">tienhuypro6789@gmail.com</label>
          </div>

          <div class="footer-right-item">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="icon icon-tabler icon-tabler-phone"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              stroke-width="2"
              stroke="currentColor"
              fill="none"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
              <path
                d="M5 4h4l2 5l-2.5 1.5a11 11 0 0 0 5 5l1.5 -2.5l5 2v4a2 2 0 0 1 -2 2a16 16 0 0 1 -15 -15a2 2 0 0 1 2 -2"
              ></path>
            </svg>
            <label class="footer-right-label">0123456789</label>
          </div>
        </div>
      </div>
    </div>

    <div class="line"></div>
    <div class="footer-bottom">
      <router-link class="footer-policy" to="/">Privacy</router-link>
      <router-link class="footer-policy" to="/">Team of use</router-link>
      <router-link class="footer-policy" to="/">Customercode</router-link>
    </div>
  </div>
</template>
<script>
export default {
  setup() {},
};
</script>

<style scoped>
.footer {
  order: 4;
  bottom: 0;
  width: 100%;
  background-color: #fff8f5;
  height: auto;
}
.footer-row-top {
  display: flex;
  justify-content: space-between;
  margin-left: 150px;
  margin-right: 100px;
}
.footer-col {
  display: flex;
  align-items: flex-start;
  width: 20%;
  flex-wrap: wrap;
  margin-top: 0px;
}
.footer-col-left {
  flex-direction: row;
  display: flex;
  flex-wrap: wrap;
  width: 200px;

  row-gap: 15px;
  text-align: left;
}
.footer-company-name {
  font-weight: bold;
  font-size: 40px;
}
.footer-company-des {
  line-height: 35px;
}
.footer-head {
  display: inline-block;
  font-weight: 800;
  font-size: 18px;
  margin-bottom: 25px;
}
.footer-item {
  display: flex;
  margin-bottom: 15px;
  text-decoration: none;
}
.footer-map {
  display: flex;
  flex-wrap: wrap;
  margin-left: -80px;
}
.iframe-map {
  height: 400px;
  width: 350px;
}
.footer-right {
  display: flex;
  flex-wrap: wrap;
}
.footer-right-item {
  display: flex;
  align-items: center;
  justify-content: left;
  margin-bottom: 20px;
}
.footer-right-label {
  margin-left: 3px;
  font-weight: 600;
}
.line {
  width: 70%;
  border-bottom: 2px solid black;
  margin: 20px 200px;
}
.footer-policy {
  text-decoration: none;
  margin-right: 10px;
  color: black;
  position: relative;
  display: inline-block;
  padding: 0 5px;
}
.footer-policy::before {
  content: "";
  position: absolute;
  top: 50%;
  left: 100%;
  width: 1px;
  height: 90%;
  background-color: black;
  transform: translate(-50%, -50%);
}
@media (max-width: 1200px) {
  .footer-map {
    display: none;
  }
  .footer-col-left {
    margin-left: 10px;
  }
  .footer-col.b {
    display: none;
  }
  .line {
    width: 0;
  }
  .footer-row-top {
    margin-left: 0px;
  }
}
.footer-bottom {
  display: none;
}
</style>
