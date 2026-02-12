import {combineReducers}  from "@reduxjs/toolkit";
import userReducer from "./slices/userSlice.ts";

export default combineReducers({
    user: userReducer,
})