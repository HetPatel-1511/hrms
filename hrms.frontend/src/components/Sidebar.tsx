import React, { useState } from 'react'
import SidebarListButton from './SidebarListButton'
import { AdjustmentsHorizontalIcon, ArrowRightEndOnRectangleIcon, BriefcaseIcon, CameraIcon, HomeIcon, UserCircleIcon } from '@heroicons/react/24/solid'
import { href } from 'react-router'

const Sidebar = React.memo(() => {
    const sidebarList = [
        {
            title: "Home",
            isDropdown: false,
            href: "/",
            Icon: <HomeIcon className='h-6 w-6' />
        },
        {
            title: "Travel Plan",
            isDropdown: true,
            Icon: <CameraIcon className='h-6 w-6' />,
            href: "/travel-plan",
            dropdown: [
                {
                    title: "Add New",
                    href: "travel-plan/add"
                }
            ]
        },
        {
            title: "Employees",
            isDropdown: false,
            href: "/employee",
            Icon: <UserCircleIcon className='h-6 w-6' />
        },
        {
            title: "Configurations",
            isDropdown: true,
            href: "/configuration",
            Icon: <AdjustmentsHorizontalIcon className='h-6 w-6' />,
            dropdown: [
                {
                    title: "Add New",
                    href: "configuration/add"
                }
            ]
        },
        {
            title: "Job Openings",
            isDropdown: true,
            href: "/job-openings",
            Icon: <BriefcaseIcon className='h-6 w-6' />,
            dropdown: [
                {
                    title: "Add New",
                    href: "job-openings/add"
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
                        <li>
                            <SidebarListButton key={index} data={sidebarItem} />
                        </li>
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
