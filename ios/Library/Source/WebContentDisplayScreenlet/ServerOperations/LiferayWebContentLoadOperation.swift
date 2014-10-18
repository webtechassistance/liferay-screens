/**
* Copyright (c) 2000-present Liferay, Inc. All rights reserved.
*
* This library is free software; you can redistribute it and/or modify it under
* the terms of the GNU Lesser General Public License as published by the Free
* Software Foundation; either version 2.1 of the License, or (at your option)
* any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
* details.
*/
import UIKit


public class LiferayWebContentLoadOperation: ServerOperation {

	public var groupId: Int64?
	public var articleId: String?

	public var resultHTML: String?


	internal override var hudLoadingMessage: HUDMessage? {
		return ("Loading...", details: "Wait few seconds...")
	}
	internal override var hudFailureMessage: HUDMessage? {
		return ("Error loading the content", details: nil)
	}


	//MARK: ServerOperation

	override func validateData() -> Bool {
		if groupId == nil {
			return false
		}

		if articleId == nil || articleId! == "" {
			return false
		}

		return true
	}

	override internal func doRun(#session: LRSession) {
		let service = LRJournalArticleService_v62(session: session)

		resultHTML = nil

		let result = service.getArticleContentWithGroupId(groupId!,
				articleId: articleId!,
				languageId: NSLocale.currentLocaleString,
				themeDisplay: nil,
				error: &lastError)

		if lastError == nil {
			resultHTML = result
		}
	}

}
