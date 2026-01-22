import {createRouter, createWebHistory} from "vue-router";
import HomeView from "../src/views/HomeView.vue";
import SignUpView from "../src/views/SignUpView.vue";
import SignInView from "../src/views/SignInView.vue";
import OtpVerificationView from "../src/views/OTPVerificationView.vue";
import PersonsView from "../src/views/PersonsView.vue";
import PersonView from "../src/views/PersonView.vue";
import CreatePersonView from "../src/views/CreatePersonView.vue";
import RecycleBinView from "../src/views/RecycleBinView.vue";
import MessageSenderView from "../src/views/MessageSenderView.vue";
import AboutView from "../src/views/AboutView.vue";
import SettingsView from "../src/views/SettingsView.vue";
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
            path: "/contacts",
            name: "contacts",
            component: PersonsView
        },
        {
            path: "/person/:id",
            name: "person",
            component: PersonView
        },
        {
            path: "/person/create",
            name: "create-person",
            component: CreatePersonView
        },
        {
            path: "/recycle-bin",
            name: "recycle-bin",
            component: RecycleBinView
        },
        {
            path: "/send-message",
            name: "send-message",
            component: MessageSenderView
        },
        {
            path: "/about",
            name: "about",
            component: AboutView
        },
        {
            path: "/settings",
            name: "settings",
            component: SettingsView
        },
        {
            path: '/:catchAll(.*)',
            name: 'not-found',
            component: NotFoundView
        }
    ]
});
