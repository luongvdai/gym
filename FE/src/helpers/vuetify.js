// Vuetify
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { VSkeletonLoader } from "vuetify/labs/VSkeletonLoader";
import { VOtpInput } from "vuetify/labs/VOtpInput";
const vuetify = createVuetify({
  components: {
    ...components,
    VSkeletonLoader,
    VOtpInput,
  },
  directives,
});

export default vuetify;
