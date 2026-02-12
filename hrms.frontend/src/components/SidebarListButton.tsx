import { useState, type ElementType, type JSX } from "react";
import { Link } from "react-router";
import { BeakerIcon } from '@heroicons/react/24/solid'

interface DropdownItem {
    title: string;
    href: string;
}

interface MenuItem {
    title: string;
    isDropdown: boolean;
    dropdown?: DropdownItem[];
    href?: string;
    Icon: React.ReactNode
}

const SidebarListButton = ({ data }: { data: MenuItem }) => {
    const [dropdownVisible, setDropdownVisible] = useState(false);
    const Icon = data.Icon;
    return (
        <>
            {data?.isDropdown ?
                <>
                    <button
                        type="button"
                        className="flex items-center p-2 w-full cursor-pointer text-base font-medium text-gray-900 rounded-lg transition duration-75 group hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                        aria-controls="dropdown-sales"
                        data-collapse-toggle="dropdown-sales"
                        onClick={() => setDropdownVisible(d => !d)}
                    >
                        {Icon}
                        <span className="flex-1 ml-3 text-left whitespace-nowrap"
                        >{data.title}</span>
                        <svg
                            aria-hidden="true"
                            className="w-6 h-6"
                            fill="currentColor"
                            viewBox="0 0 20 20"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                fill-rule="evenodd"
                                d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                clip-rule="evenodd"
                            ></path>
                        </svg>
                    </button>
                    <ul id="dropdown-sales" className={`${!dropdownVisible && "hidden"} py-2 space-y-2`}>
                        {data.dropdown?.map((d, i) =>
                            <li>
                                <DropDownItem key={i} href={d.href} title={d.title} />
                            </li>
                        )}
                    </ul>
                </> : <Link
                    to={data.href ? data.href : "/"}
                    className="flex items-center p-2 text-base font-medium text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
                >
                    {Icon}
                    <span className="ml-3">{data.title}</span>
                </Link>}
        </>
    )
}

const DropDownItem = ({ title, href }: any) => {
    return <Link
        to={href}
        className="flex items-center p-2 pl-11 w-full text-base font-medium text-gray-900 rounded-lg transition duration-75 group hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
    >{title}</Link>
}

export default SidebarListButton;