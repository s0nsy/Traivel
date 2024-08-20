import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  clickedButtons: {
    theme: false,
    location: false,
    cost: false,
    preference: false,
  },
};

const buttonSlice = createSlice({
  name: 'button',
  initialState,
  reducers: {
    clickButton: (state, action) => {
      const buttonName = action.payload;
      state.clickedButtons[buttonName] = true;
    },
    resetButtonState: (state) => {
      state.clickedButtons = {
        theme: false,
        location: false,
        cost: false,
        preference: false,
      };
    },
  },
});

export const { clickButton, resetButtonState } = buttonSlice.actions;
export default buttonSlice.reducer;
