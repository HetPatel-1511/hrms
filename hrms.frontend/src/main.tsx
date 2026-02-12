import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Provider } from 'react-redux'
import { persistor, store } from "./redux/store.ts"
import { PersistGate } from 'redux-persist/integration/react'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5*60*1000
    }
  }
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
      <QueryClientProvider client={queryClient}>
        <App />
      </QueryClientProvider>
      </PersistGate>
    </Provider>
  </StrictMode>,
)
