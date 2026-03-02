import React, { useState } from 'react'
import { Link } from 'react-router'
import { logout, selectUser } from '../redux/slices/userSlice'
import { useDispatch, useSelector } from 'react-redux'
import UserAvatar from './UserAvatar'
import { BellAlertIcon, BellIcon } from '@heroicons/react/24/solid'
import NotificationItem from './NotificationItem'
import useUnreadNotificationsQuery from '../query/queryHooks/useUnreadNotificationsQuery'
import { formatDistanceToNow } from 'date-fns'
import Notification from './Notification'
import useWebSocket from '../query/queryHooks/useWebSocket'

const Navbar = React.memo(({show=true}:any) => {
    const [showNotifications, setShowNotifications] = useState(false)
    const dispatch = useDispatch()
    const user = useSelector(selectUser);

    // fetch unread notifications and subscribe to websocket only if show is true, otherwise it will cause unnecessary re-renders and websocket connections when the component is used in a page where navbar is not shown like login or register page
    const { data, isLoading, refetch } = useUnreadNotificationsQuery(show) 
    useWebSocket({
        userId: user?.id,
        autoConnect: show,
        onMessageReceived: (notification) => {
            refetch()
        }
    });

    return (
        <nav className="bg-white border-b border-gray-200 px-4 py-2.5 dark:bg-gray-800 dark:border-gray-700 fixed left-0 right-0 top-0 z-50">
            <div className="flex flex-wrap justify-between items-center">
                <div className="flex justify-start items-center">
                    {show && <button
                        data-drawer-target="drawer-navigation"
                        data-drawer-toggle="drawer-navigation"
                        aria-controls="drawer-navigation"
                        className="p-2 mr-2 text-gray-600 rounded-lg cursor-pointer md:hidden hover:text-gray-900 hover:bg-gray-100 focus:bg-gray-100 dark:focus:bg-gray-700 focus:ring-2 focus:ring-gray-100 dark:focus:ring-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                    >
                        <svg
                            aria-hidden="true"
                            className="w-6 h-6"
                            fill="currentColor"
                            viewBox="0 0 20 20"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                fill-rule="evenodd"
                                d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h6a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                                clip-rule="evenodd"
                            ></path>
                        </svg>
                        <svg
                            aria-hidden="true"
                            className="hidden w-6 h-6"
                            fill="currentColor"
                            viewBox="0 0 20 20"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                fill-rule="evenodd"
                                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                clip-rule="evenodd"
                            ></path>
                        </svg>
                        <span className="sr-only">Toggle sidebar</span>
                    </button>}
                    <Link to="/" className="flex items-center justify-between mr-4">
                        <img
                            src="https://flowbite.s3.amazonaws.com/logo.svg"
                            className="mr-3 h-8"
                            alt="Flowbite Logo"
                        />
                        <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">Portal</span>
                    </Link>
                </div>
                {show && <div className="flex items-center lg:order-2">
                    <button
                        type="button"
                        data-dropdown-toggle="notification-dropdown"
                        className="p-2 mr-1 text-gray-500 rounded-lg dark:text-gray-400 cursor-pointer"
                        onClick={() => setShowNotifications(!showNotifications)}
                    >
                        <span className="sr-only">View notifications</span>
                        <BellIcon className='w-6 h-6' />
                        {/* a small circle with count of unread messages and it should not affect the height of navbar */}
                        {data?.data?.length > 0 && (
                            <span className="absolute w-4 h-4 inline-flex items-center justify-center text-[10px] font-bold leading-none text-white transform translate-x-1/2 -translate-y-1/2 bg-red-600 rounded-4xl">
                                {data?.data?.length}
                            </span>
                        )}
                    </button>
                    <div
                        className={`absolute top-10 right-26 overflow-hidden z-50 my-4 min-w-xs max-w-xs m text-base list-none bg-white rounded divide-y divide-gray-100 shadow-lg dark:divide-gray-600 dark:bg-gray-700 rounded-xl ${showNotifications ? '' : 'hidden'}`}
                        id="notification-dropdown"
                    >
                        <div
                            className="block border-b-1 border-b-gray-800 py-2 px-4 text-base font-medium text-center text-gray-700 bg-gray-50 dark:bg-gray-600 dark:text-gray-300"
                        >
                            Notifications
                        </div>
                        <div className='overflow-y-auto max-h-80'>
                            {showNotifications && <Notification notificationList={data?.data} isLoading={isLoading} refetch={refetch} />}
                        </div>
                    </div>
                    <button
                        type="button"
                        className="flex mx-3 text-sm bg-gray-800 rounded-full md:mr-0 cursor-pointer"
                        id="user-menu-button"
                        aria-expanded="false"
                        data-dropdown-toggle="dropdown"
                    >
                        <span className="sr-only">Open user menu</span>
                        <UserAvatar user={{image: user?.profileMedia?.url, name: user?.name}} className="w-6 h-6" bgColor="text-gray-400" />
                    </button>
                    <div
                        className="hidden z-50 my-4 w-56 text-base list-none bg-white rounded divide-y divide-gray-100 shadow dark:bg-gray-700 dark:divide-gray-600 rounded-xl"
                        id="dropdown"
                    >
                        <div className="py-3 px-4">
                            <span
                                className="block text-sm font-semibold text-gray-900 dark:text-white"
                            >Neil Sims</span>
                            <span
                                className="block text-sm text-gray-900 truncate dark:text-white"
                            >name@flowbite.com</span>
                        </div>
                        <ul
                            className="py-1 text-gray-700 dark:text-gray-300"
                            aria-labelledby="dropdown"
                        >
                            <li>
                                <a
                                    href="#"
                                    className="block py-2 px-4 text-sm hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-400 dark:hover:text-white"
                                >My profile</a>
                            </li>
                            <li>
                                <a
                                    href="#"
                                    className="block py-2 px-4 text-sm hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-400 dark:hover:text-white"
                                >Account settings</a>
                            </li>
                        </ul>
                        <ul
                            className="py-1 text-gray-700 dark:text-gray-300"
                            aria-labelledby="dropdown"
                        >
                            <li>
                                <a
                                    href="#"
                                    className="flex items-center py-2 px-4 text-sm hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                ><svg
                                    className="mr-2 w-5 h-5 text-gray-400"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                        <path
                                            fill-rule="evenodd"
                                            d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z"
                                            clip-rule="evenodd"
                                        ></path>
                                    </svg>
                                    My likes</a>
                            </li>
                            <li>
                                <a
                                    href="#"
                                    className="flex items-center py-2 px-4 text-sm hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                ><svg
                                    className="mr-2 w-5 h-5 text-gray-400"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                        <path
                                            d="M7 3a1 1 0 000 2h6a1 1 0 100-2H7zM4 7a1 1 0 011-1h10a1 1 0 110 2H5a1 1 0 01-1-1zM2 11a2 2 0 012-2h12a2 2 0 012 2v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z"
                                        ></path>
                                    </svg>
                                    Collections</a>
                            </li>
                            <li>
                                <a
                                    href="#"
                                    className="flex justify-between items-center py-2 px-4 text-sm hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                >
                                    <span className="flex items-center">
                                        <svg
                                            aria-hidden="true"
                                            className="mr-2 w-5 h-5 text-primary-600 dark:text-primary-500"
                                            fill="currentColor"
                                            viewBox="0 0 20 20"
                                            xmlns="http://www.w3.org/2000/svg"
                                        >
                                            <path
                                                fill-rule="evenodd"
                                                d="M12.395 2.553a1 1 0 00-1.45-.385c-.345.23-.614.558-.822.88-.214.33-.403.713-.57 1.116-.334.804-.614 1.768-.84 2.734a31.365 31.365 0 00-.613 3.58 2.64 2.64 0 01-.945-1.067c-.328-.68-.398-1.534-.398-2.654A1 1 0 005.05 6.05 6.981 6.981 0 003 11a7 7 0 1011.95-4.95c-.592-.591-.98-.985-1.348-1.467-.363-.476-.724-1.063-1.207-2.03zM12.12 15.12A3 3 0 017 13s.879.5 2.5.5c0-1 .5-4 1.25-4.5.5 1 .786 1.293 1.371 1.879A2.99 2.99 0 0113 13a2.99 2.99 0 01-.879 2.121z"
                                                clip-rule="evenodd"
                                            ></path>
                                        </svg>
                                        Pro version
                                    </span>
                                    <svg
                                        aria-hidden="true"
                                        className="w-5 h-5 text-gray-400"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                        xmlns="http://www.w3.org/2000/svg"
                                    >
                                        <path
                                            fill-rule="evenodd"
                                            d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                                            clip-rule="evenodd"
                                        ></path>
                                    </svg>
                                </a>
                            </li>
                        </ul>
                        <ul
                            className="py-1 text-gray-700 dark:text-gray-300"
                            aria-labelledby="dropdown"
                        >
                            <li>
                                <a
                                    href="#"
                                    className="block py-2 px-4 text-sm hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                >Sign out</a>
                            </li>
                        </ul>
                    </div>
                    
                    <button
                        type="button"
                        className="flex mx-3 text-white font-medium cursor-pointer text-sm bg-gray-800 rounded-full md:mr-0 focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-600"
                        id="user-menu-button"
                        aria-expanded="false"
                        data-dropdown-toggle="dropdown"
                        onClick={()=>dispatch(logout())}
                    >
                        Logout
                    </button>
                </div>}
            </div>
        </nav>
    )
})

export default Navbar
