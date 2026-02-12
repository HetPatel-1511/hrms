import React, { useEffect, useState } from 'react'
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import { Outlet, useNavigate } from 'react-router';
import { useSelector } from 'react-redux';
import { selectUser } from '../redux/slices/userSlice';

const UnAuthLayout = () => {
  const user = useSelector(selectUser);
  const navigate = useNavigate()
  
  useEffect(() => {
    if (user?.accessToken) {
      navigate("/")
    }
  }, [user])

  return (
    <div className="antialiased bg-gray-50 ">
      <Navbar show={false} />
      <main className="p-4">
        <Outlet />
      </main>
    </div>
  )
}

export default UnAuthLayout
