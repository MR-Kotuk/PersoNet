import {createRouter, createWebHistory} from "vue-router";
import HomeView from "../src/views/HomeView.vue";
import SignUpView from "../src/views/SignUpView.vue";
import SignInView from "../src/views/SignInView.vue";
import OtpVerificationView from "../src/views/OTPVerificationView.vue";
import NotFoundView from "../src/views/NotFoundView.vue";

export const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView
        },
        {
            path: "/sign-up",
            name: "sign-up",
            component: SignUpView
        },
        {
            path: "/sign-in",
            name: "sign-in",
            component: SignInView
        },
        {
            path: "/otp-verification",
            name: "otp-verification",
            component: OtpVerificationView
        },
        {
            path: '/:catchAll(.*)',
            name: 'not-found',
            component: NotFoundView
        }
    ]
});
