import React, { useState } from 'react'
import SidebarListButton from './SidebarListButton'
import { AdjustmentsHorizontalIcon, ArrowRightEndOnRectangleIcon, BriefcaseIcon, CameraIcon, HomeIcon, UserCircleIcon, TrophyIcon } from '@heroicons/react/24/solid'
import { href } from 'react-router'
import { useAuthorization } from '../hooks/useAuthorization'

const Sidebar = React.memo(() => {
    const {hasRole} = useAuthorization();
    const sidebarList = [
        {
            title: "Home",
            isDropdown: false,
            href: "/",
            Icon: <HomeIcon className='h-6 w-6' />,
            show: true
        },
        {
            title: "Travel Plan",
            isDropdown: false,
            Icon: <CameraIcon className='h-6 w-6' />,
            href: "/travel-plan",
            show: hasRole(["HR"]),
            dropdown: [
                {
                    title: "Add New",
                    href: "travel-plan/add"
                }
            ]
        },
        {
            title: "My Travel Plan",
            isDropdown: false,
            Icon: <CameraIcon className='h-6 w-6' />,
            href: "/travel-plan/me",
            show: true
        },
        {
            title: "Employees",
            isDropdown: false,
            href: "/employee",
            Icon: <UserCircleIcon className='h-6 w-6' />,
            show: true
        },
        {
            title: "Configurations",
            isDropdown: false,
            href: "/configuration",
            Icon: <AdjustmentsHorizontalIcon className='h-6 w-6' />,
            show: hasRole(["HR"]),
            dropdown: [
                {
                    title: "Add New",
                    href: "configuration/add"
                }
            ]
        },
        {
            title: "Job Openings",
            isDropdown: false,
            href: "/job-openings",
            Icon: <BriefcaseIcon className='h-6 w-6' />,
            show: true,
            dropdown: [
                {
                    title: "Add New",
                    href: "job-openings/add"
                }
            ]
        },
        {
            title: "Games",
            isDropdown: false,
            href: "/games",
            Icon: <TrophyIcon className='h-6 w-6' />,
            show: true,
            dropdown: [
                {
                    title: "Add New",
                    href: "games/add"
                }
            ]
        },
    ]
    return (
        <aside
            className="fixed top-0 left-0 z-40 w-64 h-screen pt-14 transition-transform -translate-x-full bg-white border-r border-gray-200 md:translate-x-0 dark:bg-gray-800 dark:border-gray-700"
            aria-label="Sidenav"
            id="drawer-navigation"
        >
            <div className="overflow-y-auto py-5 px-3 h-full bg-white dark:bg-gray-800">
                <ul className="space-y-2">
                    {sidebarList.map((sidebarItem, index) =>
                        <>
                            {sidebarItem.show && <li>
                                <SidebarListButton key={index} data={sidebarItem} />
                            </li>}
                        </>
                    )}
                </ul>
                {/* <ul
          className="pt-5 mt-5 space-y-2 border-t border-gray-200 dark:border-gray-700"
        >
        </ul> */}
            </div>
        </aside>
    )
})


export default Sidebar
