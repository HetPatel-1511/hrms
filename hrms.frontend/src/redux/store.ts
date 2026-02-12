import { configureStore, createStore } from '@reduxjs/toolkit';
import rootReducer from "./rootReducer"
import { persistReducer, persistStore } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['user']
};
const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore<any>({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);
