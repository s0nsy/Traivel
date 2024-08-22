import { createSlice } from '@reduxjs/toolkit';

const selectedItemSlice = createSlice({
  name: 'selectedItem',
  initialState: {
    region: '',
    district: '',   
    features: '',
  },
  reducers: {
    setSelectedItem: (state, action) => {
      state.region = action.payload.region;
      state.district = action.payload.district;
    },
    clearSelectedItem: () => ({
      region: '',
      district: '',
    }),
  },
});

export const { setSelectedItem, clearSelectedItem } = selectedItemSlice.actions;
export default selectedItemSlice.reducer;
