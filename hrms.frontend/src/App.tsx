import './App.css'
import { createBrowserRouter, RouterProvider } from 'react-router'
import routes from './routes'
import { useEffect } from 'react'
import axios from './axios'

function App() {
  const router = createBrowserRouter(routes);
  
  return (
    <>
    <div className="antialiased bg-gray-100 min-h-screen">
      <RouterProvider router={router} />
    </div>
    </>
  )
}

export default App
