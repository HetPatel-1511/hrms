import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { MutationCache, QueryCache, QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Provider } from 'react-redux'
import { persistor, store } from "./redux/store.ts"
import { PersistGate } from 'redux-persist/integration/react'
import { Bounce, ToastContainer, toast } from 'react-toastify';

const queryClient = new QueryClient({
  mutationCache: new MutationCache({
    onError: (error: any) => {
      toast.error(`Error: ${error.response.data.errors[0]}`);
    },
  }),
  queryCache: new QueryCache({
    onError: (error: any) => {
      toast.error(`Error: ${error.response.data.errors[0]}`);
    },
  }),
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,
      retry: 1
    }
  }
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <QueryClientProvider client={queryClient}>
          <ToastContainer
            position="top-right"
            autoClose={5000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick={false}
            rtl={false}
            draggable
            theme="light"
            transition={Bounce}
          />
          <App />
        </QueryClientProvider>
      </PersistGate>
    </Provider>
  </StrictMode>,
)
