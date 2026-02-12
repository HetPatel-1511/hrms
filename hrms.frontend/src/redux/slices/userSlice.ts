import { createSlice } from "@reduxjs/toolkit";

const initialState = { user: {} }

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    saveUser: (state, action) => {
      state.user = action.payload;
    },
    logout: (state) => {
      state.user = {};
    },
  }
});

export const { saveUser, logout } = userSlice.actions;

export const selectUser = (state: any) => state.user.user
export const selectUserEmail = (state: any) => state.user.user.email
export const selectUserRoles = (state: any) => state.user.user.roles

export default userSlice.reducer;
