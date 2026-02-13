import React from 'react';

export const Card = ({ 
  children, 
  className = '',
  hoverable = false,
  ...props 
}: any) => {
  const baseStyles = 'bg-white rounded-xl shadow';
  const hoverStyles = hoverable ? 'hover:shadow-lg transition-shadow' : '';
  
  return (
    <div 
      className={`${baseStyles} ${hoverStyles} ${className}`} 
      {...props}
    >
      {children}
    </div>
  );
};

export default Card;