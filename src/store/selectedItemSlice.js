import { createSlice } from '@reduxjs/toolkit';

const selectedItemSlice = createSlice({
  name: 'selectedItem',
  initialState: {
    region: '',
    district: '',   
    features:[],
  },
  reducers: {
    setSelectedItem: (state, action) => {
      state.region = action.payload.region;
      state.district = action.payload.district;
      state.features = action.payload.features;

    },
    clearSelectedItem: () => ({
      region: '',
      district: '',
      features:[],

    }),
  },
});

export const { setSelectedItem, clearSelectedItem } = selectedItemSlice.actions;
export default selectedItemSlice.reducer;
