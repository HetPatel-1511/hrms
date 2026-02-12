import React, { useEffect, useState } from 'react'
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import { Outlet, useNavigate } from 'react-router';
import { useSelector } from 'react-redux';
import { selectUser } from '../redux/slices/userSlice';

const AuthLayout = () => {
  const user = useSelector(selectUser);
  const navigate = useNavigate()
  
  useEffect(() => {
    if (!user?.accessToken) {
      navigate("/auth/login")
    }
  }, [user])

  return (
    <div className="antialiased bg-gray-50 ">
      <Navbar />

      <Sidebar />

      <main className="p-4 md:ml-64 h-auto pt-20 bg-gray-100">
        <Outlet />
      </main>
    </div>
  )
}

export default AuthLayout
