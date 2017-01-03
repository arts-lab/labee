/*
file of LaBeeFramework
https://www.bee-wkspace.com/

Copyright (C) 2016- Naoki Yoshioka (ARTS Laboratory)
http://www.arts-lab.net/dev/

This library is free software; you can redistribute it and/or 
modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; 
either version 3 of the License, or any later version.

This library is distributed in the hope that it will be useful, 
but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.
*/
package com.bee_wkspace.labee_fw.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 初期実行クラスである事を示すアノテーション。
 *
 * @author ARTS Laboratory
 *
 * $Id: AppInitProcess.java 559 2016-08-14 12:24:00Z pjmember $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface AppInitProcess {
	//
}
