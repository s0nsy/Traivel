import { createSlice } from '@reduxjs/toolkit';

const chatSlice = createSlice({
  name: 'chat',
  initialState: {
    userResponses: [],
  },
  reducers: {
    addUserResponse: (state, action) => {
      state.userResponses.push(action.payload);
    },
    clearResponses: (state) => {
      state.userResponses = [];
    },
  },
});

export const { addUserResponse, clearResponses } = chatSlice.actions;
export default chatSlice.reducer;  
