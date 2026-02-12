import React, { useState } from 'react'
import SidebarListButton from './SidebarListButton'
import { ArrowRightEndOnRectangleIcon, CameraIcon } from '@heroicons/react/24/solid'

const Sidebar = React.memo(() => {
    const sidebarList = [
        {
            title: "Travel Plan",
            isDropdown: false,
            href: "/travel-plan",
            Icon: <CameraIcon className='h-6 w-6' />
        },
        {
            title: "Dropdown",
            isDropdown: true,
            Icon: <ArrowRightEndOnRectangleIcon className='h-6 w-6' />,
            dropdown: [
                {
                    title: "d1",
                    href: "/d1"
                },
                {
                    title: "d2",
                    href: "/d2"
                },
            ]
        }
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
