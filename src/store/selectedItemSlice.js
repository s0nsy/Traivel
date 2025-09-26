import { createSlice } from '@reduxjs/toolkit';

const selectedItemSlice = createSlice({
  name: 'selectedItem',
  initialState: {
    destination: '',
    city: '',
    features:[],
  },
  reducers: {
    setSelectedItem: (state, action) => {
      state.destination = action.payload.destination;
      state.city = action.payload.city;
      state.features = action.payload.features;

    },
    clearSelectedItem: () => ({
      destination: '',
      city: '',
      features:[],

    }),
  },
});

export const { setSelectedItem, clearSelectedItem } = selectedItemSlice.actions;
export default selectedItemSlice.reducer;
