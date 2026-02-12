import React from 'react'
import { Link, Outlet } from 'react-router'

const Button = ({ children, textColor = "text-white", bgColor = "bg-blue-600", to, ...props }: any) => {
    return (
        <>
            {to ?
                <Link
                    className={`${textColor + " " + bgColor} cursor-pointer rounded box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none`}
                    to={to}
                    {...props}
                >
                    {children}
                </Link> :
                <button
                    className={`${textColor + " " + bgColor} cursor-pointer rounded box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none`}
                    {...props}
                >
                    {children}
                </button>}
        </>
    )
}

export default Button
